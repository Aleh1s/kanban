package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Assignee;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {

    @Query("select exists (select a from Assignee a where a.member.user.id = :userId and a.issue.id = :issueId)")
    boolean isUserAlreadyAssignedToIssue(String userId, Long issueId);

}
