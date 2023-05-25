package ua.taskmate.kanban.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.taskmate.kanban.dto.AuthenticationRequest;
import ua.taskmate.kanban.dto.AuthenticationResponse;
import ua.taskmate.kanban.security.jwt.TokenProvider;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest authenticationRequest) {
        String token = tokenProvider.generateToken(authenticationRequest);
        return new ResponseEntity<>(new AuthenticationResponse(token), HttpStatus.CREATED);
    }
}
