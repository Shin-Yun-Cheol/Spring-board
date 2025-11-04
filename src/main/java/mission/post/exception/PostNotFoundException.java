package mission.post.exception;

import mission.BusinessException;
import org.springframework.http.HttpStatus;

public class PostNotFoundException extends BusinessException {
    public PostNotFoundException() {
        super("POST_NOT_FOUND", "post not found", HttpStatus.NOT_FOUND);
    }
}
