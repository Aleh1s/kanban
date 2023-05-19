package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select distinct m from Member m left join fetch m.board")
    List<Member> findMembersByUserIdFetchBoard(String userId);
    Optional<Member> findMemberByUserId(String userId);
}
