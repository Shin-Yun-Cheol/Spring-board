package mission.user.application;

import lombok.RequiredArgsConstructor;
import mission.user.implement.UserServiceImpl;
import mission.user.dto.SignUpRequest;
import mission.user.dto.SignUpResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @PostMapping("/signup")
    public SignUpResponse signup(@RequestBody SignUpRequest request){
        return userServiceImpl.signup(request);
    }
}
