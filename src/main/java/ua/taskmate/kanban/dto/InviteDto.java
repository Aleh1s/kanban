package ua.taskmate.kanban.dto;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.MemberRole;

import java.time.LocalDateTime;

@Data
@Builder
public class InviteDto {
    private String id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private MemberRole role;
    private BoardDto board;
}
