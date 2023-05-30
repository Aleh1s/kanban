package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FullAssigneeDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private IssueDto issue;
    private MemberDto member;
}
