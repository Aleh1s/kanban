package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;

public record IdTokenRequest(
        @NotNull(message = "this field cannot be null")
        String idToken
) {
}
