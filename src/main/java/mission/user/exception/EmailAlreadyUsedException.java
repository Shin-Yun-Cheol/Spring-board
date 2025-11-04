package mission.user.exception;

import mission.BusinessException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyUsedException extends BusinessException {
    public EmailAlreadyUsedException() {
        super("USER_EMAIL_DUPLICATE", "email already in use", HttpStatus.CONFLICT);}
}
