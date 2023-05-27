package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Board;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("""
            select b from Board b 
            left join fetch b.issues i
            where b.id = :id and (i.status != 'CANCELLED' or :includeCancelled = true)
            """
    )
    Optional<Board> findBoardByIdFetchIssues(Long id, boolean includeCancelled);
}
