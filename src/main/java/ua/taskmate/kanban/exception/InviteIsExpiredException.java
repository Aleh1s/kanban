package ua.taskmate.kanban.exception;

public class InviteIsExpiredException extends RuntimeException {
    public InviteIsExpiredException(String message) {
        super(message);
    }
}
