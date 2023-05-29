package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Data;
import ua.taskmate.kanban.entity.MemberRole;

import java.time.LocalDateTime;

@Data
@Builder
public class PopulatedMemberDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private MemberRole role;
    private UserDto user;
}
