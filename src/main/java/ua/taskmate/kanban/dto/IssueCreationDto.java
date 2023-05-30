package ua.taskmate.kanban.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class IssueCreationDto {
    @NotNull(message = "this field cannot be null")
    @NotBlank(message = "this field cannot be blank")
    @Size(min = 2, max = 80, message = "length should be between 2 and 80")
    private String title;
    @NotNull(message = "this field cannot be null")
    @NotBlank(message = "this field cannot be blank")
    @Size(max = 80_000, message = "length should be between 10 and 80000")
    private String description;
    @NotNull(message = "this field cannot be null")
    @Pattern(regexp = "^BACKLOG|TO_DO|IN_PROGRESS|DONE|CANCELLED$",
            message = "status value should be from this list [BACKLOG, TO_DO, IN_PROGRESS, DONE, CANCELLED]"
    )
    private String status;
}
