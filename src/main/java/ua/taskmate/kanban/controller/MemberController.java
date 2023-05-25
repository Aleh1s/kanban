package ua.taskmate.kanban.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.taskmate.kanban.dto.FullMemberDto;
import ua.taskmate.kanban.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    public final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity<FullMemberDto> getMember(@PathVariable("memberId") Long memberId) {
        return null;
    }

}
