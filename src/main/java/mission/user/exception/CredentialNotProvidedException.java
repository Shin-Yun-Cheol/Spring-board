package mission.user.exception;

import mission.BusinessException;
import org.springframework.http.HttpStatus;

public class CredentialNotProvidedException extends BusinessException {
    public CredentialNotProvidedException() {
        super("AUTH_CREDENTIAL_NOT_PROVIDED", "email or password not found", HttpStatus.BAD_REQUEST);
    }
}