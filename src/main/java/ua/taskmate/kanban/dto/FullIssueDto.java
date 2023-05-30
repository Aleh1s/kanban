package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.taskmate.kanban.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FullIssueDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private Status status;
    private MemberDto creator;
    private List<FullCommentDto> comments;
    private List<FullAssigneeDto> assignees;
}
