package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateMemberRoleDto {
    @NotNull(message = "this field cannot be null")
    @Pattern(regexp = "^OWNER|ADMIN|MEMBER$", message = "role value should be from this list [ADMIN, OWNER, MEMBER]")
    private String role;
}
