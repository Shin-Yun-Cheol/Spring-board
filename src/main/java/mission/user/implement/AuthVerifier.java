package mission.user.implement;

import mission.user.domain.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class AuthVerifier {
    public void verifyUserEmailAndPassword(User user, String email, String password){
        if (user == null || email == null || password == null) {
            throw new IllegalArgumentException("email or password not found");
        }
        boolean ok = user.getEmail().equals(email) && BCrypt.checkpw(password, user.getPasswordHash());
        if (!ok) {
            throw new IllegalArgumentException("email or password does not match");
        }
    }
}
