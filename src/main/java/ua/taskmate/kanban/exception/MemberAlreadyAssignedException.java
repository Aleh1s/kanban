package ua.taskmate.kanban.exception;

public class MemberAlreadyAssignedException extends RuntimeException {
    public MemberAlreadyAssignedException(String message) {
        super(message);
    }
}
