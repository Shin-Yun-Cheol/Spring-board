package mission.user.implement;

import mission.user.domain.User;
import mission.user.exception.CredentialNotProvidedException;
import mission.user.exception.InvalidCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class AuthVerifier {
    public void verifyUserEmailAndPassword(User user, String email, String password){
        if (user == null || email == null || password == null) {
            throw new CredentialNotProvidedException();
        }
        boolean ok = user.getEmail().equals(email) && BCrypt.checkpw(password, user.getPasswordHash());
        if (!ok) {
            throw new InvalidCredentialsException();
        }
    }
}
