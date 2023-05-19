package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Issue;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
}
