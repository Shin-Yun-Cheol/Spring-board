package mission.user.business.command;

import jakarta.validation.constraints.NotBlank;

public record SignUpCommand(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String username
) { }
