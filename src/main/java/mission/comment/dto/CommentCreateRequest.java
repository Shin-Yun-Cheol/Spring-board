package mission.comment.dto;

public record CommentCreateRequest(
        String email,
        String password,
        String content
) {}