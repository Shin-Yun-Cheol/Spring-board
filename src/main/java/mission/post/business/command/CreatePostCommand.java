package mission.post.business.command;

import jakarta.validation.constraints.NotBlank;

public record CreatePostCommand (
    String email,
    String password,
    @NotBlank String title,
    @NotBlank String content
) {
    public static CreatePostCommand of(String email, String password, String title, String content) {
        return new CreatePostCommand(email, password, title, content);
    }
}
