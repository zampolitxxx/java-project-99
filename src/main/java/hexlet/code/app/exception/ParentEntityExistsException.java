package hexlet.code.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ParentEntityExistsException extends RuntimeException {
    public ParentEntityExistsException(String message) {
        super(message);
    }
}
