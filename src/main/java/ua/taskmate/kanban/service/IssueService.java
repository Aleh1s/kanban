package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.IssueRepository;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IssueService {

    private final BoardService boardService;
    private final IssueRepository issueRepository;
    private final MemberService memberService;

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Issue with id %d does not exist", id)));
    }

    public Issue getIssueByIdFetchCreatorAndCommentsAndAssignees(Long id) {
        Issue issue = getIssueById(id);
        Hibernate.initialize(issue.getCreator());
        Hibernate.initialize(issue.getComments());
        Hibernate.initialize(issue.getAssignees());
        return issue;
    }

    @Transactional
    public void save(Long boardId, Issue issue) {
        Board board = boardService.getBoardById(boardId);

        // todo: check if this user has grant to update role

        String userId = "some_id"; //todo:
        Member member = memberService.getMemberByUserId(userId);

        member.addIssue(issue);
        board.addIssue(issue);

        issueRepository.save(issue);
    }

    @Transactional
    public void update(Long issueId, Long boardId, Issue updatedIssue) {
        Board board = boardService.getBoardById(boardId);

        // todo: check if this user has grant to update role

        Issue issueToUpdate = getIssueById(issueId);
        issueToUpdate.setTitle(updatedIssue.getTitle());
        issueToUpdate.setDescription(updatedIssue.getDescription());
        issueToUpdate.setStatus(updatedIssue.getStatus());
        issueToUpdate.setUpdatedAt(LocalDateTime.now());
    }
}
