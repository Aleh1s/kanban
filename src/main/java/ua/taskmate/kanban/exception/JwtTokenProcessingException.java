package ua.taskmate.kanban.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class JwtTokenProcessingException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String reason;
}
