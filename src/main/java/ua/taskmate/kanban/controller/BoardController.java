package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.*;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.entity.MemberRole;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.BoardService;
import ua.taskmate.kanban.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final Mapper mapper;

    @PostMapping()
    public ResponseEntity<?> createBoard(@RequestBody @Valid BoardCreationDto boardCreationDto,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.save(mapper.toBoard(boardCreationDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoardById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable("id") Long id,
                                         @RequestBody @Valid BoardCreationDto updatedBoard,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.updateBoardById(id, mapper.toBoard(updatedBoard));
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{boardId}/members/{memberId}")
    public ResponseEntity<?> updateMemberRole(@PathVariable("boardId") Long boardId,
                                              @PathVariable("memberId") Long memberId,
                                              @Valid @RequestBody MemberRoleDto memberRoleDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.updateRole(memberId, boardId, MemberRole.valueOf(memberRoleDto.getRole()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<BoardDto>> getBoardsOfCurrentUser() {
        List<Board> boards = boardService.getBoardsOfCurrentUser();
        List<BoardDto> boardDtoList = boards.stream()
                .map(mapper::toBoardDto)
                .toList();
        return ResponseEntity.ok(boardDtoList);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long id) {
        boardService.deleteMemberById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<FullBoardDto> getBoard(
            @PathVariable("boardId") Long boardId,
            @RequestParam(value = "includeCancelled", required = false, defaultValue = "false") boolean includeCancelled
    ) {
        Board board = boardService.getBoardByIdFetchMembersAndIssues(boardId, includeCancelled);
        return ResponseEntity.ok(mapper.toFullBoardDto(board));
    }

    @GetMapping("/{boardId}/members")
    public ResponseEntity<List<PopulatedMemberDto>> getMembers(@PathVariable("boardId") Long boardId) {
        List<Member> members = memberService.getMembersByBoardId(boardId);
        return ResponseEntity.ok(memberService.populateMembers(members));
    }
}
