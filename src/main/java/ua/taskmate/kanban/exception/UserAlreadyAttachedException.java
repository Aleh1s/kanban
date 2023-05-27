package ua.taskmate.kanban.exception;

public class UserAlreadyAttachedException extends RuntimeException {
    public UserAlreadyAttachedException(String message) {
        super(message);
    }
}
