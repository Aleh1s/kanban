package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;

public record CodeRequest(
        @NotNull
        String code
) {

}
