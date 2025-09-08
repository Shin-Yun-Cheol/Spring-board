package mission.comment.dto;

public record CommentResponse(
        Long commentId,
        String email,
        String content
) {}