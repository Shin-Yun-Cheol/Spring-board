package mission.user.application;

import lombok.RequiredArgsConstructor;
import mission.user.business.UserService;
import mission.user.application.dto.SignUpRequest;
import mission.user.application.dto.SignUpResponse;
import mission.user.business.command.SignUpCommand;
import mission.user.business.result.SignUpResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponse signup(@RequestBody SignUpRequest req) {
        var cmd = new SignUpCommand(req.email(), req.password(), req.username());
        SignUpResult result = userService.signup(cmd);
        return new SignUpResponse(result.email(), result.username());
    }
}
