package mission.comment.dto;

public record CommentDeleteRequest(
        String email,
        String password
) {}