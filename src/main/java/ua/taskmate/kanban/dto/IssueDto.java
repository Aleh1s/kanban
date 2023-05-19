package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;
import ua.taskmate.kanban.entity.Status;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class IssueDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String title;
    private String description;
    private Status status;
    private MemberDto creator;
    private List<CommentDto> comments;
    private List<AssigneeDto> assignees;
}
