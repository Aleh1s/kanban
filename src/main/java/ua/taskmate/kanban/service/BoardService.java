package ua.taskmate.kanban.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.taskmate.kanban.entity.*;
import ua.taskmate.kanban.exception.IllegalActionException;
import ua.taskmate.kanban.exception.ResourceNotFoundException;
import ua.taskmate.kanban.repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;

    public Board getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Board with id %d does not exist!", id)));
    }

    public Board getBoardByIdFetchMembersFetchIssues(Long id, boolean includeCancelledIssues) {
        Board board = boardRepository.findBoardByIdFetchIssues(id, includeCancelledIssues)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Board with id %d does not exist!", id)));
        Hibernate.initialize(board.getMembers());
        return board;
    }

    @Transactional
    public void save(Board board) {
        Member owner = Member.builder()
                .role(MemberRole.OWNER)
                .userId("some_id")
                .build();

        boardRepository.save(board);
        board.addMember(owner);
        memberService.save(owner);
    }

    @Transactional
    public void deleteBoardById(Long id) {
        Board boardToDelete = getBoardById(id);

        boolean isUserOwner = boardToDelete.getMembers().stream()
                .filter(member -> member.getRole().equals(MemberRole.OWNER))
                .anyMatch(member -> member.getUserId().equals("some_id"));

        if (!isUserOwner) {
            throw new IllegalActionException(
                    "You have not grant to delete this board. Only owner can delete this board!");
        }

        boardRepository.delete(boardToDelete);
    }

    @Transactional
    public void updateBoardById(Long id, Board updatedBoard) {
        Board boardToUpdate = getBoardById(id);
        boardToUpdate.setName(updatedBoard.getName());
        boardToUpdate.setImageUrl(updatedBoard.getImageUrl());
        boardToUpdate.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void updateRole(Long memberId, Long boardId, MemberRole role) {
        Board board = getBoardById(boardId);

        // todo: check if this user has grant to update role

        Member member = memberService.getMemberById(memberId);
        member.setRole(role);
    }

    public List<Board> getBoardsOfCurrentUser() {
        String userId = "some_id"; //todo: get userId from auth context
        List<Member> members = memberService.getMembersByUserIdFetchBoard(userId);
        return members.stream()
                .map(Member::getBoard)
                .collect(Collectors.toList());
    }

    public void deleteMember(Long boardId, Long memberId) {
        Board board = getBoardById(boardId);

        // todo: check if this user has grant to update role

        memberService.deleteMemberById(memberId);
    }
}
