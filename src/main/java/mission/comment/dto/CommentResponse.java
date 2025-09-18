package mission.comment.dto;

import mission.comment.Comment;

public record CommentResponse(
        Long commentId,
        Long postId,
        Long authorId,
        String content
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getPost().getId(), comment.getAuthor().getId(), comment.getContent());
    }
}