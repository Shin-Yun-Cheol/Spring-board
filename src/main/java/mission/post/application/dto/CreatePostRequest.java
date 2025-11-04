package mission.post.application.dto;

import jakarta.validation.constraints.NotBlank;
import mission.post.business.command.CreatePostCommand;

public record CreatePostRequest(
        String email,
        String password,
        @NotBlank String title,
        @NotBlank String content
){
    public CreatePostCommand toCommand(){
        return CreatePostCommand.of(email, password, title, content);
    }
}