package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;

public record CodeRequest(
        @NotNull(message = "this field cannot be null")
        String code
) {

}
