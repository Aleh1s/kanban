package ua.taskmate.kanban.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.taskmate.kanban.entity.Invite;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String> {
}
