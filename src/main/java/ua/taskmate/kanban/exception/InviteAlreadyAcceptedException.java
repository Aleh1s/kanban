package ua.taskmate.kanban.exception;

public class InviteAlreadyAcceptedException extends RuntimeException {
    public InviteAlreadyAcceptedException(String message) {
        super(message);
    }
}
