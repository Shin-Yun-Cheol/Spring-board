package mission.comment.dto;

public record CommentUpdateRequest(
        String email,
        String password,
        String content
) {}
