package mission.post.application.dto;

public record PostCreateResponse(
        Long articleId,
        String email,
        String title,
        String content
) {}