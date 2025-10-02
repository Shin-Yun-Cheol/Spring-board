package mission.post.business.command;

import jakarta.validation.constraints.NotBlank;

public record CreatePostCommand (
    String email,
    String password,
    @NotBlank String title,
    @NotBlank String content
) {}
