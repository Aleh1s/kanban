package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InviteCreationDto(
        @NotNull(message = "this field cannot be null")
        @Pattern(regexp = "^OWNER|ADMIN|MEMBER$", message = "role value should be from this list [ADMIN, OWNER, MEMBER]")
        String memberRole
) {
}
