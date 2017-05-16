package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.error.exception.NoPermissionException;
import com.intive.patronage.toz.users.UserRepository;
import com.intive.patronage.toz.util.RepositoryChecker;
import com.intive.patronage.toz.util.UserInfoGetter;
import com.intive.patronage.toz.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
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

    List<Comment> findAllComments(UUID petUuid, Boolean shortened, Comment.State state) {
        List<Comment> commentList;
        if (state != null && petUuid != null) {
            commentList = commentRepository.findByPetUuidAndStateOrderByCreatedDesc(petUuid,
                    state);
        } else if (state != null && petUuid == null) {
            commentList = commentRepository.findByStateOrderByCreatedDesc(state);
        } else if (state == null && petUuid != null) {
            commentList = commentRepository.findByPetUuidOrderByCreatedDesc(petUuid);
        } else {
            commentList = commentRepository.findAllByOrderByCreatedDesc();
        }
        return createShortenedCommentContents(shortened, commentList);
    }

    Comment findById(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, commentRepository, COMMENT);
        return commentRepository.findOne(id);
    }

    Comment createComment(Comment comment) {
        UUID userUuid = UserInfoGetter.getUserUuid(userRepository, USER);
        comment.setUserUuid(userUuid);
        comment.setState(STATE_ACTIVE);
        return commentRepository.save(comment);
    }

    void deleteComment(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, commentRepository, COMMENT);
        if (!UserInfoGetter.hasCurrentUserAdminRole()) {
            UUID userUuid = UserInfoGetter.getUserUuid(userRepository, USER);
            checkPermissionToManageComment(id, userUuid);
        }
        Comment comment = commentRepository.getOne(id);
        comment.setState(STATE_DELETE);
        commentRepository.save(comment);
    }

    Comment updateComment(final UUID id, final Comment comment) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, commentRepository, COMMENT);
        comment.setId(id);
        UUID userUuid;
        if (UserInfoGetter.hasCurrentUserAdminRole()) {
            userUuid = commentRepository.findOne(id).getUserUuid();
        } else {
            userUuid = UserInfoGetter.getUserUuid(userRepository, USER);
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
}
