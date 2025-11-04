package mission.user.exception;

import mission.BusinessException;
import org.springframework.http.HttpStatus;


public class EmailNotFoundException extends BusinessException {
    public EmailNotFoundException() {
        super("EMAIL_NOT_FOUND", "email not found", HttpStatus.NOT_FOUND);
    }
}

