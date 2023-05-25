package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.AssigneeCreationDto;
import ua.taskmate.kanban.dto.CommentCreationDto;
import ua.taskmate.kanban.dto.FullIssueDto;
import ua.taskmate.kanban.dto.IssueCreationDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.AssigneeService;
import ua.taskmate.kanban.service.CommentService;
import ua.taskmate.kanban.service.IssueService;

@RestController()
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueController {

    private final AssigneeService assigneeService;
    private final IssueService issueService;
    private final CommentService commentService;
    private final Mapper mapper;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<?> createIssue(@PathVariable("boardId") Long boardId,
                                         @Valid @RequestBody IssueCreationDto issueCreationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        issueService.save(boardId, mapper.toIssue(issueCreationDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{issueId}")
    public ResponseEntity<?> updateIssue(@PathVariable("issueId") Long issueId,
                                         @Valid @RequestBody IssueCreationDto issueCreationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        issueService.update(issueId, mapper.toIssue(issueCreationDto));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullIssueDto> getIssue(@PathVariable("id") Long id) {
        Issue issue = issueService.getIssueByIdFetchCreatorAndCommentsAndAssignees(id);
        FullIssueDto fullIssueDto = mapper.toFullIssueDto(issue);
        return new ResponseEntity<>(fullIssueDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIssue(@PathVariable("id") Long id) {
        issueService.deleteIssueById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{issueId}/assignees")
    public ResponseEntity<?> addAssignee(@PathVariable("issueId") Long issueId,
                                         @RequestBody @Valid AssigneeCreationDto assigneeCreationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        assigneeService.createAssignee(issueId, assigneeCreationDto.memberId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{assigneeId}")
    public ResponseEntity<?> deleteAssignee(@PathVariable("assigneeId") Long assigneeId) {
        assigneeService.deleteAssigneeById(assigneeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{issueId}/comments")
    public ResponseEntity<?> addComment(@PathVariable("issueId") Long issueId,
                                        @Valid @RequestBody CommentCreationDto commentCreationDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        commentService.save(issueId, commentCreationDto.creatorId(), mapper.toComment(commentCreationDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable("commentId") Long commentId,
                                           @RequestBody @Valid CommentCreationDto commentCreationDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        commentService.editComment(commentId, mapper.toComment(commentCreationDto));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteCommentById(commentId);
        return ResponseEntity.noContent().build();
    }
}
