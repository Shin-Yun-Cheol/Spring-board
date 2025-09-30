package mission.user.business;

import mission.user.dto.SignUpRequest;
import mission.user.dto.SignUpResponse;

public interface UserService {
    SignUpResponse signup(SignUpRequest signUpRequest);
}
