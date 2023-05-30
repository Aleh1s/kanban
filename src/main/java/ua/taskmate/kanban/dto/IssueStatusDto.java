package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record IssueStatusDto(
        @NotNull(message = "this field cannot be null")
        @Pattern(regexp = "^BACKLOG|TO_DO|IN_PROGRESS|DONE|CANCELLED$",
                message = "status value should be from this list [BACKLOG, TO_DO, IN_PROGRESS, DONE, CANCELLED]"
        )
        String status
) { }
