package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.comment.model.view.CommentView;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.util.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = ApiUrl.COMMENTS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    private final CommentService commentService;

    @Autowired
    CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS')")
    public ResponseEntity<List<CommentView>> getAllComments(
            @RequestParam(value = "petUuid", required = false) UUID petUuid,
            @RequestParam(value = "shortened", required = false, defaultValue = "false")
                    Boolean shortened) {
        final List<Comment> commentList = commentService.findAllComments(petUuid, shortened);
        return ResponseEntity.ok().body(ModelMapper.convertToView(commentList, CommentView.class));
    }

    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER', 'ANONYMOUS')")
    public ResponseEntity<CommentView> getCommentById(@PathVariable UUID id) {
        return ResponseEntity.ok().
                body(ModelMapper.convertToView(commentService.findById(id), CommentView.class));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<CommentView> createComment(@Valid @RequestBody CommentView commentView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(commentService.createComment(convertFromView(commentView)),
                        CommentView.class));
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<?> deleteCommentById(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<CommentView> updateComment(@PathVariable UUID id,
                                                     @Valid @RequestBody CommentView commentView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(commentService.
                        updateComment(id, convertFromView(commentView)), CommentView.class));
    }

    private static Comment convertFromView(final CommentView commentView) {
        return ModelMapper.convertToModel(commentView, Comment.class);
    }
}
