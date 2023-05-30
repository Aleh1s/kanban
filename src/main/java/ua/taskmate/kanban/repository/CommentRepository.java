package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
            select distinct c from Comment c
            left join fetch c.creator
            left join fetch c.issue
            where c.issue.id = :issueId
            """)
    List<Comment> findCommentsByIssueIdFetchCreatorAndIssue(Long issueId);
}
