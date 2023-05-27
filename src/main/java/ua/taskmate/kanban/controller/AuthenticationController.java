package ua.taskmate.kanban.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ua.taskmate.kanban.constant.ApplicationConstant;
import ua.taskmate.kanban.dto.CodeRequest;
import ua.taskmate.kanban.dto.TokenResponse;
import ua.taskmate.kanban.dto.UrlResponse;
import ua.taskmate.kanban.exception.ValidationException;
import ua.taskmate.kanban.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.redirectUri}")
    private String redirectUri;

    @GetMapping("/url")
    public ResponseEntity<UrlResponse> getOAuthUrl() {
        UriComponentsBuilder url = UriComponentsBuilder.fromHttpUrl(ApplicationConstant.AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email");
        return new ResponseEntity<>(new UrlResponse(url.toUriString()), HttpStatus.OK);
    }

    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> authorize(@RequestBody @Valid CodeRequest codeRequest,
                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors());
        }
        String jwtToken = authenticationService.authenticate(codeRequest);
        return new ResponseEntity<>(new TokenResponse(jwtToken), HttpStatus.OK);
    }
}
