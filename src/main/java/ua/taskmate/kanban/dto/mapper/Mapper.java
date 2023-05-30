package ua.taskmate.kanban.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.taskmate.kanban.dto.*;
import ua.taskmate.kanban.entity.*;
import ua.taskmate.kanban.service.MemberService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final MemberService memberService;

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

    public FullIssueDto toFullIssueDto(Issue issue, List<Assignee> assignees, List<Comment> comments) {
        List<FullCommentDto> commentsDtoList = comments.stream()
                .map(this::toFullCommentDto).toList();
        List<FullAssigneeDto> assigneesDtoList = assignees.stream()
                .map(this::toFullAssigneeDto).toList();
        return FullIssueDto.builder()
                .id(issue.getId())
                .createdAt(issue.getUpdatedAt())
                .updatedAt(issue.getUpdatedAt())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .creator(toMemberDto(issue.getCreator()))
                .comments(commentsDtoList)
                .assignees(assigneesDtoList)
                .build();
    }

    private FullCommentDto toFullCommentDto(Comment comment) {
        return FullCommentDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .creator(toMemberDto(comment.getCreator()))
                .issue(toIssueDto(comment.getIssue()))
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
                .build();
    }

    public FullAssigneeDto toFullAssigneeDto(Assignee assignee) {
        return FullAssigneeDto.builder()
                .id(assignee.getId())
                .createdAt(assignee.getCreatedAt())
                .updatedAt(assignee.getUpdatedAt())
                .issue(toIssueDto(assignee.getIssue()))
                .member(toMemberDto(assignee.getMember()))
                .build();
    }

    public Comment toComment(CommentCreationDto dto) {
        return Comment.builder()
                .content(dto.content())
                .build();
    }

    public Invite toInvite(InviteCreationDto dto) {
        return Invite.builder()
                .role(MemberRole.valueOf(dto.memberRole()))
                .build();
    }

    public InviteDto toInviteDto(Invite invite) {
        return InviteDto.builder()
                .id(invite.getId())
                .createdAt(invite.getCreatedAt())
                .updatedAt(invite.getUpdatedAt())
                .isAccepted(invite.isAccepted())
                .role(invite.getRole())
                .build();
    }

    public FullBoardDto toFullBoardDto(Board board) {
        List<IssueDto> issues = board.getIssues().stream()
                .map(this::toIssueDto)
                .toList();
        List<PopulatedMemberDto> populateMembers = memberService.populateMembers(board.getMembers());
        return FullBoardDto.builder()
                .id(board.getId())
                .name(board.getName())
                .imageUrl(board.getImageUrl())
                .updatedAt(board.getUpdatedAt())
                .createdAt(board.getCreatedAt())
                .members(populateMembers)
                .issues(issues)
                .build();
    }

    private IssueDto toIssueDto(Issue issue) {
        return IssueDto.builder()
                .id(issue.getId())
                .createdAt(issue.getCreatedAt())
                .updatedAt(issue.getUpdatedAt())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .build();
    }

    public FullMemberDto toFullMemberDto(Member member) {
        UserDto user = toUserDto(member.getUser());
        BoardDto board = toBoardDto(member.getBoard());
        List<IssueDto> issues = member.getIssues().stream()
                .map(this::toIssueDto)
                .toList();
        List<CommentDto> comments = member.getComments().stream()
                .map(this::toCommentDto)
                .toList();
        return FullMemberDto.builder()
                .id(member.getId())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .role(member.getRole())
                .user(user)
                .board(board)
                .issues(issues)
                .comments(comments)
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFistName())
                .lastName(user.getLastName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
