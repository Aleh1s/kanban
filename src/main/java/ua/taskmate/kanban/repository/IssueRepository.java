package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Issue;

import java.util.Optional;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    @Query("select i from Issue i left join fetch i.creator where i.id = :issueId")
    Optional<Issue> findIssueByIdFetchCreator(Long issueId);
}
