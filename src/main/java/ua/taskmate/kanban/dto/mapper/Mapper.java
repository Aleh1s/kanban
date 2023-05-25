package ua.taskmate.kanban.dto.mapper;

import org.springframework.stereotype.Component;
import ua.taskmate.kanban.dto.*;
import ua.taskmate.kanban.entity.*;

import java.util.List;

@Component
public class Mapper {

    public Board toBoard(BoardCreationDto dto) {
        return Board.builder()
                .name(dto.name())
                .imageUrl(dto.imageUrl())
                .build();
    }

    public BoardDto toBoardDto(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .name(board.getName())
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }

    public Issue toIssue(IssueCreationDto dto) {
        return Issue.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(Status.valueOf(dto.getStatus()))
                .build();
    }

    public FullIssueDto toFullIssueDto(Issue issue) {
        List<CommentDto> comments = issue.getComments().stream()
                .map(this::toCommentDto).toList();
        List<AssigneeDto> assignees = issue.getAssignees().stream()
                .map(this::toAssigneeDto).toList();
        MemberDto creator = toMemberDto(issue.getCreator());
        return FullIssueDto.builder()
                .id(issue.getId())
                .updatedAt(issue.getUpdatedAt())
                .createdAt(issue.getUpdatedAt())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .creator(creator)
                .comments(comments)
                .assignees(assignees)
                .build();
    }

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .updatedAt(comment.getUpdatedAt())
                .createdAt(comment.getUpdatedAt())
                .content(comment.getContent())
                .build();
    }

    public MemberDto toMemberDto(Member member) {
        return MemberDto.builder()
                .id(member.getId())
                .updatedAt(member.getUpdatedAt())
                .createdAt(member.getCreatedAt())
                .role(member.getRole())
                .userId(member.getUserId())
                .build();
    }

    public AssigneeDto toAssigneeDto(Assignee assignee) {
        return AssigneeDto.builder()
                .id(assignee.getId())
                .createdAt(assignee.getCreatedAt())
                .updatedAt(assignee.getUpdatedAt())
                .build();

    }

    public Comment toComment(CommentCreationDto dto) {
        return Comment.builder()
                .content(dto.content())
                .build();
    }

}
