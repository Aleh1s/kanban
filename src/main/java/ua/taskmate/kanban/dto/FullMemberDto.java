package ua.taskmate.kanban.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ua.taskmate.kanban.entity.MemberRole;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class FullMemberDto {
    private Long id;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private MemberRole role;
    private UserDto user;
    private BoardDto board;
    private List<IssueDto> issues;
    private List<CommentDto> comments;
}
