package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoardCreationDto(
        @NotNull(message = "this field cannot be null")
        @NotBlank(message = "this field cannot be blank")
        String name,
        @NotNull(message = "this field cannot be null")
        @NotBlank(message = "this field cannot be blank")
        String imageUrl
) {

}