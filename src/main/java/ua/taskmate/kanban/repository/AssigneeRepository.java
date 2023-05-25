package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Assignee;

@Repository
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
}
