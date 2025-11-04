package mission.user.implement;

import mission.user.application.dto.SignUpRequest;
import mission.user.business.command.SignUpCommand;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public SignUpCommand toCommand(SignUpRequest req){
        return new SignUpCommand(req.email(), req.password(),  req.username());
    }
}
