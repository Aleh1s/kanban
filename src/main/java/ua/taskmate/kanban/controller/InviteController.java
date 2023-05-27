package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.InviteCreationDto;
import ua.taskmate.kanban.dto.InviteDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.Invite;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.InviteService;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;
    private final Mapper mapper;

    @PostMapping("/boards/{boardId}")
    public ResponseEntity<InviteDto> createInvite(@PathVariable("boardId") Long boardId,
                                                  @RequestBody @Valid InviteCreationDto inviteCreationDto,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        Invite invite = mapper.toInvite(inviteCreationDto);
        inviteService.saveInvite(boardId, invite);
        return new ResponseEntity<>(mapper.toInviteDto(invite), HttpStatus.OK);
    }

    @PostMapping("/{inviteId}")
    public ResponseEntity<?> acceptInvite(@PathVariable("inviteId") String inviteId) {
        inviteService.acceptInvite(inviteId);
        return ResponseEntity.ok().build();
    }
}
