package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.entity.Status;
import ua.taskmate.kanban.exception.ActionWithoutRightsException;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.IssueRepository;
import ua.taskmate.kanban.util.Util;

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

    public Issue getIssueByIdFetchCreator(Long issueId) {
        return issueRepository.findIssueByIdFetchCreator(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found!"));
    }

    @Transactional
    public void save(Long boardId, Issue issue) {
        String userId = Util.getPrincipal().getUsername();
        Board board = boardService.getBoardById(boardId);
        if (!hasRightToCreateIssue(userId, board)) {
            throw new ActionWithoutRightsException("You have no rights to create issue on this board!");
        }
        Member member = memberService.getMemberByUserIdAndBoardId(userId, boardId);
        member.addIssue(issue);
        board.addIssue(issue);
        issueRepository.save(issue);
    }

    @Transactional
    public void update(Long issueId, Issue updatedIssue) {
        UserDetails principal = Util.getPrincipal();
        Issue issueToUpdate = getIssueById(issueId);
        if (hasNoRightForIssue(principal.getUsername(), issueToUpdate)) {
            throw new ActionWithoutRightsException("You have no rights to update this issue!");
        }
        issueToUpdate.setTitle(updatedIssue.getTitle());
        issueToUpdate.setDescription(updatedIssue.getDescription());
        issueToUpdate.setStatus(updatedIssue.getStatus());
        issueToUpdate.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void deleteIssueById(Long id) {
        String userId = Util.getPrincipal().getUsername();
        Issue issue = getIssueById(id);
        Board board = issue.getBoard();
        if (hasNoRightForIssue(userId, issue)) {
            throw new ActionWithoutRightsException("You have no rights to delete this issue!");
        }
        board.deleteIssue(issue);
    }

    @Transactional
    public void updateIssueStatus(Long issueId, Status status) {
        Issue issue = getIssueById(issueId);
        issue.setStatus(status);
    }

    private boolean hasRightToCreateIssue(String userId, Board board) {
        return memberService.existsMemberByUserIdAndBoardId(userId, board.getId());
    }

    private boolean hasNoRightForIssue(String userId, Issue issue) {
        return !issue.getCreator().getUser().getId().equals(userId);
    }
}
