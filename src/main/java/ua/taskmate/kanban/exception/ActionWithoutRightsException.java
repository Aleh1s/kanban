package ua.taskmate.kanban.exception;

public class ActionWithoutRightsException extends RuntimeException {

    public ActionWithoutRightsException(String message) {
        super(message);
    }
}
