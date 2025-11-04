package mission.post.application.dto;

import mission.post.business.result.PostResult;

public record PostCreateResponse(
        Long articleId,
        String email,
        String title,
        String content
) {
    public static PostCreateResponse of(PostResult result){
        return new PostCreateResponse(result.articleId(),  result.email(), result.title(), result.content());
    }
}