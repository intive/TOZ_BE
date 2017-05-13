package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.error.exception.NoPermissionException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.error.exception.WrongEnumValueException;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.util.RolesChecker;
import com.intive.patronage.toz.util.StringFormatter;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private static final Integer SHORTENED_COMMENT_CONTENTS_LENGTH = 220;
    private static final Comment.State STATE_ACTIVE = Comment.State.ACTIVE;
    private static final Comment.State STATE_DELETE = Comment.State.DELETED;
    private static final String COMMENT = "Comment";
    private static final String USER = "User";

    @Autowired
    CommentService(CommentRepository commentRepository,
                   UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    List<Comment> findAllComments(UUID petUuid, Boolean shortened, String state) {
        checkIfEnumIsValid(state);
        List<Comment> commentList;
        if (state != null && petUuid != null) {
            commentList = commentRepository.findByPetUuidAndStateOrderByCreatedDesc(petUuid,
                    Comment.State.valueOf(state));
        } else if (state != null && petUuid == null) {
            commentList = commentRepository.findByStateOrderByCreatedDesc(Comment.State.
                    valueOf(state));
        } else if (state == null && petUuid != null) {
            commentList = commentRepository.findByPetUuidOrderByCreatedDesc(petUuid);
        } else {
            commentList = commentRepository.findAllByOrderByCreatedDesc();
        }
        return createShortenedCommentContents(shortened, commentList);
    }

    private void checkIfEnumIsValid(String state) {
        if (state != null && !EnumUtils.isValidEnum(Comment.State.class, state)) {
            throw new WrongEnumValueException(Comment.State.class);
        }
    }

    Comment findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return commentRepository.findOne(id);
    }

    Comment createComment(Comment comment) {
        UUID userUuid = getUserUuid();
        comment.setUserUuid(userUuid);
        comment.setState(STATE_ACTIVE);
        return commentRepository.save(comment);
    }

    void deleteComment(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        if (!RolesChecker.hasCurrentUserAdminRole()) {
            UUID userUuid = getUserUuid();
            checkPermissionToManageComment(id, userUuid);
        }
        Comment comment = commentRepository.getOne(id);
        comment.setState(STATE_DELETE);
        commentRepository.save(comment);
    }

    Comment updateComment(final UUID id, final Comment comment) {
        throwNotFoundExceptionIfNotExists(id);
        comment.setId(id);
        UUID userUuid;
        if (RolesChecker.hasCurrentUserAdminRole()) {
            userUuid = commentRepository.findOne(id).getUserUuid();
        } else {
            userUuid = getUserUuid();
            checkPermissionToManageComment(id, userUuid);
            comment.setState(STATE_ACTIVE);
        }
        comment.setUserUuid(userUuid);
        return commentRepository.save(comment);
    }

    private void checkPermissionToManageComment(UUID id, UUID userUuid) {
        if (!commentRepository.findOne(id).getUserUuid().toString()
                .equals(userUuid.toString())) {
            throw new NoPermissionException();
        }
    }

    private UUID getUserUuid() {
        final Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        UUID userUuid = null;
        if (authentication != null && authentication.getPrincipal() != null) {
            final UserContext userContext = (UserContext) authentication
                    .getPrincipal();
            userUuid = userContext.getUserId();
            ifEntityDoesNotExistInRepoThrowException(userUuid,
                    userRepository, USER);
        }
        return userUuid;
    }

    private List<Comment> createShortenedCommentContents(
            Boolean shortened, List<Comment> commentList) {
        if (shortened) {
            for (Comment comment : commentList) {
                comment.setContents(StringFormatter
                        .trimToLengthPreserveWord(comment.getContents(),
                                SHORTENED_COMMENT_CONTENTS_LENGTH));
            }
        }
        return commentList;
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!commentRepository.exists(id)) {
            throw new NotFoundException(COMMENT);
        }
    }

    private void ifEntityDoesNotExistInRepoThrowException(
            UUID id, IdentifiableRepository repo, String entityName) {
        if (!repo.exists(id)) {
            throw new NotFoundException(entityName);
        }
    }
}
