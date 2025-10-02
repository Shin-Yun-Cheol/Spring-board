package mission.user.business;

import mission.user.domain.User;
import mission.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import mission.user.business.command.SignUpCommand;
import mission.user.business.result.SignUpResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class) // Mockito 기능을 사용하기 위한 설정
class UserServiceTest {

    @InjectMocks
    private UserService userService; // 테스트의 주인공

    @Mock
    private UserRepository userRepository; // 가짜로 대체할 의존 객체

    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given (준비)
        SignUpCommand command = new SignUpCommand("test@example.com", "password123", "testuser");

        // userRepository.existsByEmail이 false를 반환하도록 설정 (중복 이메일 없음)
        given(userRepository.existsByEmail(command.email())).willReturn(false);

        // userRepository.save가 호출되면, 저장된 User 객체를 반환하도록 설정
        User savedUser = User.builder()
                .email(command.email())
                .passwordHash("hashed_password") // BCrypt 로직은 UserService에 있으므로 아무 해시값이나 사용
                .username(command.username())
                .build();
        given(userRepository.save(any(User.class))).willReturn(savedUser);

        // when (실행)
        SignUpResult result = userService.signup(command);

        // then (검증)
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo(command.email());
        assertThat(result.username()).isEqualTo(command.username());

        // userRepository의 save 메서드가 정확히 1번 호출되었는지 검증
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입 시 예외 발생")
    void signup_fail_with_duplicate_email() {
        // given (준비)
        SignUpCommand command = new SignUpCommand("test@example.com", "password123", "testuser");

        // userRepository.existsByEmail이 true를 반환하도록 설정 (중복 이메일 있음)
        given(userRepository.existsByEmail(command.email())).willReturn(true);

        // when & then (실행 및 검증)
        // userService.signup을 실행했을 때 IllegalArgumentException이 발생하는지 검증
        assertThrows(IllegalArgumentException.class, () -> userService.signup(command));

        // 예외가 발생했으므로 save 메서드는 절대 호출되지 않아야 함
        verify(userRepository, never()).save(any(User.class));
    }


}