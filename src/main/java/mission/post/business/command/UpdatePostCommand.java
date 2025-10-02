package mission.post.business.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePostCommand(
        @NotNull
        Long id,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String title,
        @NotBlank
        String content
) {}
