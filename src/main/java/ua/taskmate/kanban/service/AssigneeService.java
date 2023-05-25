package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Assignee;
import ua.taskmate.kanban.entity.Issue;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.repository.AssigneeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;
    private final MemberService memberService;
    private final IssueService issueService;

    @Transactional
    public void createAssignee(Long issueId, Long memberId) {
        Issue issue = issueService.getIssueById(issueId);
        Member member = memberService.getMemberById(memberId);
        Assignee assignee = new Assignee();
        issue.addAssignee(assignee);
        member.addAssignee(assignee);
        assigneeRepository.save(assignee);
    }

    @Transactional
    public void deleteAssigneeById(Long assigneeId) {
        assigneeRepository.deleteById(assigneeId);
    }
}
