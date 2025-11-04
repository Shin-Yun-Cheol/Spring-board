package mission.user.exception;

import mission.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationValidatorTest {

    @Mock UserRepository userRepository;
    @InjectMocks UserRegistrationValidator validator;

    @Test
    void throws_when_email_exists() {
        when(userRepository.existsByEmail("dup@ex.com")).thenReturn(true);
        assertThrows(EmailAlreadyUsedException.class,
                () -> validator.assertEmailAvailable("dup@ex.com"));
    }

    @Test
    void noop_when_null_or_blank() {
        validator.assertEmailAvailable(null);
        validator.assertEmailAvailable("");
        validator.assertEmailAvailable("  ");
        verify(userRepository, never()).existsByEmail(anyString());
    }
}
