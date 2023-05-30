package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Comment;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.ActionWithoutRightsException;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.CommentRepository;
import ua.taskmate.kanban.util.Util;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final IssueService issueService;

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Comment with userId %d does not exist", id)));
    }

    @Transactional
    public void save(Long issueId, Long memberId, Comment comment) {
        Issue issue = issueService.getIssueById(issueId);
        Member member = memberService.getMemberById(memberId);
        issue.addComment(comment);
        member.addComment(comment);
        commentRepository.save(comment);
    }

    @Transactional
    public void editComment(Long commentId, Comment updatedComment) {
        UserDetails principal = Util.getPrincipal();
        Comment commentToUpdate = getCommentById(commentId);
        if (hasNoRightForComment(principal.getUsername(), commentToUpdate)) {
            throw new ActionWithoutRightsException("You have no grant to edit comment!");
        }
        commentToUpdate.setContent(updatedComment.getContent());
    }

    @Transactional
    public void deleteCommentById(Long commentId) {
        UserDetails principal = Util.getPrincipal();
        Comment commentToDelete = getCommentById(commentId);
        if (hasNoRightForComment(principal.getUsername(), commentToDelete)) {
            throw new ActionWithoutRightsException("You have no grant to delete comment!");
        }
        commentRepository.deleteById(commentId);
    }


    public List<Comment> getCommentsByIssueIdFetchCreatorAndIssue(Long issueId) {
        return commentRepository.findCommentsByIssueIdFetchCreatorAndIssue(issueId);
    }

    private boolean hasNoRightForComment(String userId, Comment comment) {
        return !comment.getCreator()
                .getUser()
                .getId()
                .equals(userId);
    }
}
