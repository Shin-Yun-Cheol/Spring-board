package mission.user.implement;

import lombok.RequiredArgsConstructor;
import mission.user.domain.User;
import mission.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {
    private final UserRepository userRepository;

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("email not found"));
    }
}
