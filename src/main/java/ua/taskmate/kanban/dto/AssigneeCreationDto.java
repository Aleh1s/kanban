package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public record AssigneeCreationDto(
        @NotNull(message = "this field cannot be null")
        Long memberId
) {
}
