package mission.user.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.user.business.command.SignUpCommand;
import mission.user.business.result.SignUpResult;
import mission.user.domain.User;
import mission.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignUpResult signup(SignUpCommand signUpCommand) {
        if(userRepository.existsByEmail(signUpCommand.email())){
            throw new IllegalArgumentException("email already in use");
        }

        String hashedPassword = BCrypt.hashpw(signUpCommand.password(), BCrypt.gensalt());
        User saved = userRepository.save(
                User.builder()
                        .email(signUpCommand.email())
                        .passwordHash(hashedPassword)
                        .username(signUpCommand.username())
                        .build()
        );
        return new SignUpResult(saved.getEmail(), saved.getUsername());
    }
}
