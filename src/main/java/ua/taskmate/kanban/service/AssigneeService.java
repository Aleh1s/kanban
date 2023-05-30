package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Assignee;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.MemberAlreadyAssignedException;
import ua.taskmate.kanban.repository.AssigneeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;
    private final MemberService memberService;
    private IssueService issueService;

    @Transactional
    public void createAssignee(Long issueId, Long memberId) {
        Issue issue = issueService.getIssueById(issueId);
        Member member = memberService.getMemberById(memberId);
        if (isUserAlreadyAssignedToIssue(member.getUser().getId(), issueId)) {
            throw new MemberAlreadyAssignedException("Member is already assigned to this issue!");
        }
        Assignee assignee = new Assignee();
        issue.addAssignee(assignee);
        member.addAssignee(assignee);
        assigneeRepository.save(assignee);
    }

    @Transactional
    public void deleteAssigneeById(Long assigneeId) {
        assigneeRepository.deleteById(assigneeId);
    }

    public List<Assignee> getAssigneesByIssueIdFetchIssueAndMember(Long issueId) {
        return assigneeRepository.findAssigneesByIssueIdFetchIssueAndMember(issueId);
    }

    public boolean isUserAlreadyAssignedToIssue(String userId, Long issueId) {
        return assigneeRepository.isUserAlreadyAssignedToIssue(userId, issueId);
    }

    @Autowired
    public void setIssueService(@Lazy IssueService issueService) {
        this.issueService = issueService;
    }

}
