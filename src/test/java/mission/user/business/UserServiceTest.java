package mission.user.business;

import mission.user.business.command.SignUpCommand;
import mission.user.business.result.SignUpResult;
import mission.user.domain.User;
import mission.user.exception.EmailAlreadyUsedException;
import mission.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserService userService;

    @Test
    void signup_success() {
        // given
        SignUpCommand cmd = new SignUpCommand("a@b.com", "pw", "nick");
        User saved = new User("a@b.com", "$2a$10$hash", "nick");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        // when
        SignUpResult result = userService.signup(cmd);

        // then
        assertEquals("a@b.com", result.email());
        assertEquals("nick", result.username());
    }

    @Test
    void signup_duplicate_email_translates_to_EmailAlreadyUsedException() {
        // given
        SignUpCommand cmd = new SignUpCommand("dup@b.com", "pw", "nick");
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("duplicate"));

        // expect
        assertThrows(EmailAlreadyUsedException.class, () -> userService.signup(cmd));
    }
}
