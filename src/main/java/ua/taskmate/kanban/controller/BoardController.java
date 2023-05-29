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
    public ResponseEntity<HttpStatus> createBoard(@RequestBody @Valid BoardCreationDto boardCreationDto,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.save(mapper.toBoard(boardCreationDto));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBoard(@PathVariable("id") Long id) {
        boardService.deleteBoardById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBoard(@PathVariable("id") Long id,
                                         @RequestBody @Valid BoardCreationDto updatedBoard,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.updateBoardById(id, mapper.toBoard(updatedBoard));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{boardId}/members/{memberId}")
    public ResponseEntity<HttpStatus> updateMemberRole(@PathVariable("boardId") Long boardId,
                                              @PathVariable("memberId") Long memberId,
                                              @Valid @RequestBody MemberRoleDto memberRoleDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.updateRole(memberId, boardId, MemberRole.valueOf(memberRoleDto.getRole()));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping()
    public ResponseEntity<List<BoardDto>> getBoardsOfCurrentUser() {
        List<Board> boards = boardService.getBoardsOfCurrentUser();
        List<BoardDto> boardDtoList = boards.stream()
                .map(mapper::toBoardDto)
                .toList();
        return new ResponseEntity<>(boardDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<HttpStatus> deleteMember(@PathVariable("id") Long id) {
        boardService.deleteMemberById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<FullBoardDto> getBoard(
            @PathVariable("boardId") Long boardId,
            @RequestParam(value = "includeCancelled", required = false, defaultValue = "false") boolean includeCancelled
    ) {
        Board board = boardService.getBoardByIdFetchMembersAndIssues(boardId, includeCancelled);
        return new ResponseEntity<>(mapper.toFullBoardDto(board), HttpStatus.OK);
    }

    @GetMapping("/{boardId}/members")
    public ResponseEntity<List<PopulatedMemberDto>> getMembers(@PathVariable("boardId") Long boardId) {
        List<Member> members = memberService.getMembersByBoardId(boardId);
        return new ResponseEntity<>(memberService.populateMembers(members), HttpStatus.OK);
    }
}
