package ua.taskmate.kanban.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.taskmate.kanban.dto.RestExceptionDto;
import ua.taskmate.kanban.exception.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<RestExceptionDto> handleValidationException(ValidationException exception) {
        Map<String, List<String>> errors = exception.getErrors().stream()
                .map(objectError -> (FieldError) objectError)
                .collect(groupingBy(FieldError::getField, mapping(
                        DefaultMessageSourceResolvable::getDefaultMessage, toList())));
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(errors).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<RestExceptionDto> handleResourceNotFoundException(ResourceNotFoundException exception){
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(exception.getMessage()).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActionWithoutRightsException.class)
    public ResponseEntity<RestExceptionDto> handleActionWithoutRights(ActionWithoutRightsException exception){
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(exception.getMessage()).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(InviteAlreadyAcceptedException.class)
    public ResponseEntity<RestExceptionDto> handleInviteAlreadyAccepted(InviteAlreadyAcceptedException exception) {
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(exception.getMessage()).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyAttachedException.class)
    public ResponseEntity<RestExceptionDto> handleUserAlreadyAttached(UserAlreadyAttachedException exception) {
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(exception.getMessage()).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InviteIsExpiredException.class)
    public ResponseEntity<RestExceptionDto> handleInviteIsExpired(InviteIsExpiredException exception) {
        RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                .payload(exception.getMessage()).build();
        return new ResponseEntity<>(restExceptionDto, HttpStatus.BAD_REQUEST);
    }
}
