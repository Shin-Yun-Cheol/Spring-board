package mission.post.business.result;

import mission.post.domain.Post;

public record PostResult(
        Long articleId,
        String email,
        String title,
        String content
) {
    public static PostResult of(Post post) {
        return new PostResult(
                post.getId(),
                post.getAuthor().getEmail(),
                post.getTitle(),
                post.getContent()
        );
    }
}
