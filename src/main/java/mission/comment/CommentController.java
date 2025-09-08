package mission.comment;

import mission.comment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request
    ) {
        return commentService.create(postId, request);
    }

    @PutMapping("/comments/{id}")
    public CommentResponse updateComment(
            @PathVariable("id") Long commentId,
            @RequestBody CommentUpdateRequest request
    ) {
        return commentService.update(commentId, request);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @RequestBody CommentDeleteRequest request
    ) {
        commentService.delete(commentId, request);
    }
}
