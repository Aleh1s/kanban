package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
