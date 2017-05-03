package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.util.StringFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private static final Integer COMMENT_CONTENTS_LENGTH = 100;
    private static final String COMMENT = "Comment";

    @Autowired
    CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    List<Comment> findAllComments(UUID petUuid, Boolean shortened) {
        List<Comment> commentList;
        if (petUuid == null) {
            commentList = commentRepository.findAll();
        } else {
            commentList = commentRepository.findByPetUuid(petUuid);
        }
        return createShortenedCommentContents(shortened, commentList);
    }

    Comment findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return commentRepository.findOne(id);
    }

    Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    void deleteComment(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        commentRepository.delete(id);
    }

    Comment updateComment(final UUID id, final Comment comment) {
        throwNotFoundExceptionIfNotExists(id);
        comment.setId(id);
        return commentRepository.save(comment);
    }

    private List<Comment> createShortenedCommentContents(Boolean shortened,
                                                         List<Comment> commentList) {
        if (shortened) {
            for (Comment comment : commentList) {
                comment.setContents(StringFormatter.
                        trimToLengthPreserveWord(comment.getContents(),
                                COMMENT_CONTENTS_LENGTH));
            }
        }
        return commentList;
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!commentRepository.exists(id)) {
            throw new NotFoundException(COMMENT);
        }
    }
}
