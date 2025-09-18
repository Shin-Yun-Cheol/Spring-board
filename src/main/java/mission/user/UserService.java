package mission.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.user.dto.DeleteUserRequest;
import mission.user.dto.SignUpRequest;
import mission.user.dto.SignUpResponse;
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

    @Transactional
    public void deleteAccount(DeleteUserRequest deleteRequest) {
        User user = userRepository.findByEmail(deleteRequest.email())
                .orElseThrow(() -> new IllegalArgumentException("email not found"));

        if(!BCrypt.checkpw(deleteRequest.password(), user.getPasswordHash())){
            throw new IllegalArgumentException("password does not match");
        }

        userRepository.delete(user);
    }
}
