package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AssigneeDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private MemberDto member;
}
