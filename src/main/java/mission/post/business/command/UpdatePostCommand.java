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
) {
        public static UpdatePostCommand of(Long id, String email, String password, String title, String content) {
                return new UpdatePostCommand(id, email, password, title, content);
        }
}
