package mission.user;

import lombok.RequiredArgsConstructor;
import mission.user.dto.DeleteUserRequest;
import mission.user.dto.SignUpRequest;
import mission.user.dto.SignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignUpResponse signup(@RequestBody SignUpRequest request){
        return userService.signup(request);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMe(@RequestBody DeleteUserRequest request){
        userService.deleteAccount(request);
    }
}
