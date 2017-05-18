package com.intive.patronage.toz.comment;

import com.intive.patronage.toz.comment.model.db.Comment;
import com.intive.patronage.toz.comment.model.view.CommentView;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.exception.NoPermissionException;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.util.ModelMapper;
import com.intive.patronage.toz.util.UserInfoGetter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(tags = "Comments", description = "Comment operations.")
@RestController
@RequestMapping(value = ApiUrl.COMMENTS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class CommentController {
    private final static Comment.State DEFAULT_TYPE = Comment.State.ACTIVE;
    private final CommentService commentService;

    @Autowired
    CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ApiOperation(value = "Get all comments.", responseContainer = "List", notes =
            "Required roles: SA, TOZ, VOLUNTEER.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ') or " +
            "(hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER') and (#state == T(com.intive.patronage.toz" +
            ".comment.model.db.Comment.State).ACTIVE or #state == null))")
    public ResponseEntity<List<CommentView>> getAllComments(
            @RequestParam(value = "petUuid", required = false) UUID petUuid,
            @RequestParam(value = "isShortened", required = false, defaultValue = "false")
                    Boolean isShortened,
            @RequestParam(value = "state", required = false) Comment.State state) {
        final List<Comment> newsList;
        if (UserInfoGetter.hasCurrentUserAdminRole()) {
            newsList = commentService.findAllComments(petUuid, isShortened, state);
            return ResponseEntity.ok().body(ModelMapper.convertIdentifiableToView(newsList, CommentView.class));
        }
        newsList = commentService.findAllComments(petUuid, isShortened, DEFAULT_TYPE);
        return ResponseEntity.ok().body(ModelMapper.convertIdentifiableToView(newsList, CommentView.class));
    }

    @ApiOperation(value = "Get comment by id.", notes =
            "Required roles: SA, TOZ, VOLUNTEER for ACTIVE type, or " +
                    "SA, TOZ for other types.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Comment not found.", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public CommentView getCommentById(@PathVariable UUID id) {
        Comment comment = commentService.findById(id);
        if (UserInfoGetter.hasCurrentUserAdminRole() || comment.getState().equals(Comment.State.ACTIVE)) {
            return ModelMapper.convertToView(comment, CommentView.class);
        } else {
            throw new NoPermissionException();
        }
    }

    @ApiOperation(value = "Create comment.", response = CommentView.class, notes =
            "Required roles: SA, TOZ, VOLUNTEER.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request.",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found.", response = ErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<CommentView> createComment(@Valid @RequestBody CommentView commentView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(commentService
                        .createComment(convertFromView(commentView)), CommentView.class));
    }

    @ApiOperation(value = "Delete comment.", notes =
            "Required roles: SA, TOZ, VOLUNTEER.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Comment/User not found.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Permission denied.",
                    response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<?> deleteCommentById(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update comment.", response = CommentView.class, notes =
            "Required roles: SA, TOZ, VOLUNTEER.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Comment/User not found.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "Permission denied.",
                    response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request.",
                    response = ValidationErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ', 'VOLUNTEER')")
    public ResponseEntity<CommentView> updateComment(@PathVariable UUID id,
                                                     @Valid @RequestBody CommentView commentView) {
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(baseLocation)
                .body(ModelMapper.convertToView(commentService
                        .updateComment(id, convertFromView(commentView)), CommentView.class));
    }

    private static Comment convertFromView(final CommentView commentView) {
        return ModelMapper.convertToModel(commentView, Comment.class);
    }
}
