package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.Invite;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.exception.*;
import ua.taskmate.kanban.repository.InviteRepository;
import ua.taskmate.kanban.util.Util;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    public Invite getInviteById(String id) {
        return inviteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invite not found"));
    }

    @Transactional
    public void saveInvite(Long boardId, Invite invite) {
        UserDetails principal = Util.getPrincipal();
        Board board = boardService.getBoardById(boardId);
        if (!boardService.hasRightToUpdateBoard(principal.getUsername(), board)) {
            throw new ActionWithoutRightsException("You have no grant to create invite for this board!");
        }
        board.addInvite(invite);
        inviteRepository.save(invite);
    }

    @Transactional
    public void acceptInvite(String inviteId) {
        Invite invite = getInviteById(inviteId);
        if (invite.isAccepted()) {
            throw new InviteAlreadyAcceptedException("Invite is already accepted!");
        }
        if (invite.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InviteIsExpiredException("This invite is already expired!");
        }
        UserDetails principal = Util.getPrincipal();
        String userId = principal.getUsername();
        Board board = invite.getBoard();
        List<Member> members = board.getMembers();
        boolean userAlreadyAttached = members.stream()
                .anyMatch(member -> member.getUserId().equals(userId));
        if (userAlreadyAttached) {
            throw new UserAlreadyAttachedException("You are already attached to this board!");
        }
        Member member = Member.builder()
                .role(invite.getRole())
                .userId(userId)
                .build();
        board.addMember(member);
        memberService.saveMember(member);
        invite.setAccepted(true);
    }
}
