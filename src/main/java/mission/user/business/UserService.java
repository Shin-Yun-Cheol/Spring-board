package mission.user.business;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mission.user.business.command.SignUpCommand;
import mission.user.business.result.SignUpResult;
import mission.user.domain.User;
import mission.user.exception.EmailAlreadyUsedException;
import mission.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public SignUpResult signup(SignUpCommand signUpCommand) {
        String hashedPassword = BCrypt.hashpw(signUpCommand.password(), BCrypt.gensalt());
        try {
            User saved = userRepository.save(
                    User.of(signUpCommand, hashedPassword)
            );
            return new SignUpResult(saved.getEmail(), saved.getUsername());
        } catch (RuntimeException e) {
            throw new EmailAlreadyUsedException();
        }
    }
}
