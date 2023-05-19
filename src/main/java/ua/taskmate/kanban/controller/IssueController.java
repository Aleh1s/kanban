package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.FullIssueDto;
import ua.taskmate.kanban.dto.IssueCreationDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.IssueService;

@RestController()
@RequestMapping("/api/v1/issues")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;
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

    @PutMapping("/{issueId}/boards/{boardId}")
    public ResponseEntity<?> updateIssue(@PathVariable("issueId") Long issueId,
                                         @PathVariable("boardId") Long boardId,
                                         @Valid @RequestBody IssueCreationDto issueCreationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        issueService.update(issueId, boardId, mapper.toIssue(issueCreationDto));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullIssueDto> getIssue(@PathVariable("id") Long id) {
        Issue issue = issueService.getIssueByIdFetchCreatorAndCommentsAndAssignees(id);
        FullIssueDto fullIssueDto = mapper.toFullIssueDto(issue);
        return new ResponseEntity<>(fullIssueDto, HttpStatus.OK);
    }
}
