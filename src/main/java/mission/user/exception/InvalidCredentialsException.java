package mission.user.exception;

import mission.BusinessException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("AUTH_INVALID_CREDENTIALS", "email or password does not match", HttpStatus.UNAUTHORIZED);
    }
}