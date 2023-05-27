package ua.taskmate.kanban.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.taskmate.kanban.dto.FullMemberDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.Member;
import ua.taskmate.kanban.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;
    public final Mapper mapper;
    @GetMapping()
    public ResponseEntity<FullMemberDto> getMember(@RequestParam(value = "memberId", required = false) Long memberId,
                                                   @RequestParam(value = "boardId", required = false) Long boardId) {
        Member member = memberService.getMemberByIdOrUserIdAndBoardIdFetchAll(memberId, boardId);
        return ResponseEntity.ok(mapper.toFullMemberDto(member));
    }
}
