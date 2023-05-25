package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreationDto(
        @NotNull(message = "this field cannot be null")
        @NotBlank(message = "this field cannot be null")
        @Size(min = 2, max = 200, message = "field length should be between 2 and 200")
        String content,
        @NotNull(message = "this field cannot be null")
        Long creatorId
) {
}
