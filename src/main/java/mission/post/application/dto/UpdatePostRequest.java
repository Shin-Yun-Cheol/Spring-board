package mission.post.application.dto;

import jakarta.validation.constraints.NotBlank;
import mission.post.business.command.UpdatePostCommand;

public record UpdatePostRequest(
        String email,
        String password,
        @NotBlank String title,
        @NotBlank String content
) {
    public UpdatePostCommand toCommand(Long id){
        return UpdatePostCommand.of(id, email, password, title, content);
    }
}
