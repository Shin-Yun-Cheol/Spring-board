package mission.user.exception;

import lombok.RequiredArgsConstructor;
import mission.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegistrationValidator {
    private final UserRepository userRepository;

    public void assertEmailAvailable(String email) {
        if (email == null || email.isBlank()) return;
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyUsedException();
        }
    }
}
