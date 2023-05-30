package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Assignee;

import java.util.List;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {

    @Query("select exists (select a from Assignee a where a.member.user.id = :userId and a.issue.id = :issueId)")
    boolean isUserAlreadyAssignedToIssue(String userId, Long issueId);

    @Query("""
            select distinct a from Assignee a
            left join fetch a.issue
            left join fetch a.member
            where a.issue.id = :issueId
            """)
    List<Assignee> findAssigneesByIssueIdFetchIssueAndMember(Long issueId);
}
