package mission.comment;

import mission.comment.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse addComment(@PathVariable Long postId,
                                      @RequestParam Long userId,
                                      @RequestBody CommentCreateRequest req) {
        return commentService.create(postId, userId, req);
    }

    @PutMapping("/comments/{id}")
    public CommentResponse updateComment(
            @PathVariable("id") Long commentId,
            @RequestParam Long userId,
            @RequestBody CommentUpdateRequest request
    ) {
        return commentService.update(commentId, userId, request);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @RequestParam Long userId
    ) {
        commentService.delete(commentId, userId);
    }
}
