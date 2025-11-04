package mission.user.implement;

import mission.user.domain.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

class AuthVerifierTest {

    AuthVerifier verifier = new AuthVerifier();

    /*@Test
    void throws_when_missing_params() {
        assertThrows(IllegalArgumentException.class,
                () -> verifier.verifyUserEmailAndPassword(null, null, null));
    }*/

   /* @Test
    void throws_when_password_not_match() {
        String hash = BCrypt.hashpw("pw", BCrypt.gensalt());
        User u = new User("a@b.com", hash, "nick");
        assertThrows(IllegalArgumentException.class,
                () -> verifier.verifyUserEmailAndPassword(u, "a@b.com", "wrong"));
    }*/

    @Test
    void ok_when_all_match() {
        String hash = BCrypt.hashpw("pw", BCrypt.gensalt());
        User u = new User("a@b.com", hash, "nick");
        assertDoesNotThrow(() -> verifier.verifyUserEmailAndPassword(u, "a@b.com", "pw"));
    }
}
