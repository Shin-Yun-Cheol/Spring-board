package mission.post.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdatePostRequest(
        String email,
        String password,
        @NotBlank String title,
        @NotBlank String content
) {
}
