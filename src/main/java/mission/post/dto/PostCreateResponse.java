package mission.post.dto;

public record PostCreateResponse(
        Long articleId,
        String email,
        String title,
        String content
) {}