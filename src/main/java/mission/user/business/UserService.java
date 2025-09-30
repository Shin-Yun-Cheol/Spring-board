package mission.user.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.user.domain.User;
import mission.user.dto.SignUpRequest;
import mission.user.dto.SignUpResponse;
import mission.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignUpResponse signup(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.email())){
            throw new IllegalArgumentException("email already in use");
        }

        String hashedPassword = BCrypt.hashpw(signUpRequest.password(), BCrypt.gensalt());
        User saved = userRepository.save(
                User.builder()
                        .email(signUpRequest.email())
                        .passwordHash(hashedPassword)
                        .username(signUpRequest.username())
                        .build()
        );
        return new SignUpResponse(saved.getEmail(), saved.getUsername());
    }
}
