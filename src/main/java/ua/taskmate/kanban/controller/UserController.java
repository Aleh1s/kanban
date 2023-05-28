package ua.taskmate.kanban.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.taskmate.kanban.dto.UserDto;
import ua.taskmate.kanban.dto.mapper.Mapper;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Mapper mapper;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId) {
        User user = userService.getUserById(userId);
        return new ResponseEntity<>(mapper.toUserDto(user), HttpStatus.OK);
    }
}
