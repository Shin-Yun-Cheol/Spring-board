package mission.post.business.command;

public record CreatePostCommand (
    String email,
    String password,
    String title,
    String content
) {
    public CreatePostCommand {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title is null or empty");
        if (content == null || content.isBlank()) throw new IllegalArgumentException("content is null or empty");
    }
}
