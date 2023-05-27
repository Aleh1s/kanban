package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
}
