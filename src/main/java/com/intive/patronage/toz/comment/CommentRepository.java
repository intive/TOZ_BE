package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.base.repository.IdentifiableRepository;
import com.intive.patronage.toz.comment.model.db.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends IdentifiableRepository<Comment> {
    List<Comment> findByPetUuid(UUID petUuid);
    List<Comment> findByPetUuidOrderByCreatedDesc(UUID petUuid);
    List<Comment> findAllByOrderByCreatedDesc();
    List<Comment> findByStateOrderByCreatedDesc(Comment.State state);
    List<Comment> findByPetUuidAndStateOrderByCreatedDesc(UUID petUuid, Comment.State state);
}
