package mission.post.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreatePostRequest(
        String email,
        String password,
        @NotBlank String title,
        @NotBlank String content
){}