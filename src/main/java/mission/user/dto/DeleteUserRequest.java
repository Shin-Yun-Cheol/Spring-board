package mission.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DeleteUserRequest(
        @Email @NotBlank String email,
        @NotBlank String password
) {}
