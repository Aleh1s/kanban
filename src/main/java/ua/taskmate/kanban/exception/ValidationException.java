package ua.taskmate.kanban.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.ObjectError;

import java.util.List;
@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {
    private final List<ObjectError> errors;
}
