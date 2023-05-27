package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.entity.MemberRole;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.exception.ActionWithoutRightsException;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.BoardRepository;
import ua.taskmate.kanban.util.Util;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final UserService userService;

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Board with id %d does not exist!", id)));
    }

    @Transactional
    public void save(Board board) {
        String userId = Util.getPrincipal().getUsername();
        User user = userService.getUserById(userId);
        Member owner = Member.builder()
                .role(MemberRole.OWNER)
                .build();
        user.addMember(owner);
        boardRepository.save(board);
        board.addMember(owner);
        memberService.saveMember(owner);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        UserDetails principal = Util.getPrincipal();
        Board boardToDelete = getBoardById(id);
        if (!hasRightToDeleteBoard(principal.getUsername(), boardToDelete)) {
            throw new ActionWithoutRightsException(
                    "You have no grant to delete this board. Only owner can delete this board!");
        }
        boardRepository.delete(boardToDelete);
    }

    @Transactional
    public void updateBoardById(Long id, Board updatedBoard) {
        UserDetails principal = Util.getPrincipal();
        Board boardToUpdate = getBoardById(id);
        if (!hasRightToUpdateBoard(principal.getUsername(), boardToUpdate)) {
            throw new ActionWithoutRightsException(
                    "You have no grant to update this board. Only owner and admin can update this board!");
        }
        boardToUpdate.setName(updatedBoard.getName());
        boardToUpdate.setImageUrl(updatedBoard.getImageUrl());
        boardToUpdate.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void updateRole(Long memberId, Long boardId, MemberRole role) {
        UserDetails principal = Util.getPrincipal();
        Board board = getBoardById(boardId);
        if (!hasRightToUpdateBoard(principal.getUsername(), board)) {
            throw new ActionWithoutRightsException(
                    "You have no grant to change role of member of this board. Only owner and admin can do this!");
        }
        Member member = memberService.getMemberById(memberId);
        member.setRole(role);
    }

    public List<Board> getBoardsOfCurrentUser() {
        UserDetails principal = Util.getPrincipal();
        List<Member> members = memberService.getMembersByUserIdFetchBoard(principal.getUsername());
        return members.stream()
                .map(Member::getBoard)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMemberById(Long id) {
        String userId = Util.getPrincipal().getUsername();
        Member member = memberService.getMemberById(id);
        Board board = member.getBoard();
        if (!hasRightToUpdateBoard(userId, board)) {
            throw new ActionWithoutRightsException(
                    "You have no grant to delete member from this board. Only owner and admin can do this!");
        }
        board.deleteMember(member);
    }


    public boolean hasRightToUpdateBoard(String userId, Board board) {
        return hasRightToUpdateBoard(userService.getUserById(userId), board);
    }

    private boolean hasRightToDeleteBoard(String userId, Board board) {
        return hasRightToDeleteBoard(userService.getUserById(userId), board);
    }

    private boolean hasRightToUpdateBoard(User user, Board board) {
        Optional<Member> member = memberService.findMemberByUserIdAndBoardId(user.getId(), board.getId());
        if (member.isPresent()) {
            MemberRole memberRole = member.get().getRole();
            return memberRole.equals(MemberRole.OWNER) || memberRole.equals(MemberRole.ADMIN);
        }
        return false;
    }

    private boolean hasRightToDeleteBoard(User user, Board board) {
        Optional<Member> member = memberService.findMemberByUserIdAndBoardId(user.getId(), board.getId());
        return member.map(value -> value.getRole().equals(MemberRole.OWNER)).orElse(false);
    }

    public Board getBoardByIdFetchMembersAndIssues(Long boardId, boolean includeCancelled) {
        Board board = boardRepository.findBoardByIdFetchIssues(boardId, includeCancelled)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found!"));
        Hibernate.initialize(board.getMembers());
        return board;
    }
}
