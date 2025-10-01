package mission.post.business.result;

public record PostResult(
        Long articleId,
        String email,
        String title,
        String content
) {
}
