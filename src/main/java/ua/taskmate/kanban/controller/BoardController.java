package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.BoardCreationDto;
import ua.taskmate.kanban.dto.BoardDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.dto.UpdateMemberRoleDto;
import ua.taskmate.kanban.entity.Board;
import ua.taskmate.kanban.entity.MemberRole;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.BoardService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boards")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
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
                                              @Valid @RequestBody UpdateMemberRoleDto updateMemberRoleDto,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        boardService.updateRole(memberId, boardId, MemberRole.valueOf(updateMemberRoleDto.getRole()));
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<BoardDto>> getBoardsOfCurrentUser() {
        List<Board> boards = boardService.getBoardsOfCurrentUser();
        List<BoardDto> boardDtoList = boards.stream()
                .map(mapper::toBoardDto).toList();
        return ResponseEntity.ok(boardDtoList);
    }

    @DeleteMapping("/{boardId}/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable("boardId") Long boardId,
                                          @PathVariable("memberId") Long memberId) {
        boardService.deleteMember(boardId, memberId);
        return ResponseEntity.noContent().build();
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<BoardDto> getBoard(
//            @PathVariable("id") Long boardId,
//            @RequestParam(value = "includeCancelled", required = false, defaultValue = "false") boolean includeCancelled
//    ) {
//        Board board = boardService.getBoardByIdFetchMembersFetchIssues(boardId, includeCancelled);
//        return ResponseEntity.ok(boardMapper.toDto(board));
//    }
}
