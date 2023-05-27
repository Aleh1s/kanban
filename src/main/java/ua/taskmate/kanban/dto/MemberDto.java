package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.taskmate.kanban.entity.MemberRole;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MemberDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private MemberRole role;
}
