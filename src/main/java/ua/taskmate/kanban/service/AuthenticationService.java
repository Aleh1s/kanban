package ua.taskmate.kanban.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ua.taskmate.kanban.constant.ApplicationConstant;
import ua.taskmate.kanban.dto.CodeRequest;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.exception.AuthenticationException;
import ua.taskmate.kanban.exception.ForbiddenException;
import ua.taskmate.kanban.security.jwt.TokenProvider;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger log = LogManager.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final TokenProvider tokenProvider;

    @Value("${security.oauth2.client.id}")
    private String clientId;
    @Value("${security.oauth2.client.secret}")
    private String clientSecret;
    @Value("${security.oauth2.redirectUri}")
    private String redirectUri;
    @Transactional
    public String authenticate(CodeRequest codeRequest) {
        String idToken = getIdToken(codeRequest.code())
                .orElseThrow(() -> new ForbiddenException("Unable to get token!"));
        User user = parseUser(idToken);
        if (userService.existsUserBySub(user.getSub())) {
            userService.updateUserBySub(user.getSub(), user);
        } else {
            userService.saveUser(user);
        }
        return tokenProvider.generateToken(user);
    }

    private Optional<String> getIdToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", ApplicationConstant.GRANT_TYPE);
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response;
        try {
             response = restTemplate.postForEntity(ApplicationConstant.TOKEN_URL, request, String.class);
        } catch (RestClientException e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationException("Something went wrong during authorization. Please try again later.");
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(objectMapper.readTree(response.getBody())
                    .get("id_token")
                    .asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private User parseUser(String idToken) {
        Claims claims = Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(idToken.substring(0, idToken.lastIndexOf('.') + 1))
                .getBody();
        return claimsToUser(claims);
    }

    private User claimsToUser(Claims claims) {
        return User.builder()
                .sub((String) claims.get("sub"))
                .email((String) claims.get("email"))
                .profileImageUrl((String) claims.get("picture"))
                .fistName((String) claims.get("given_name"))
                .lastName((String) claims.get("family_name"))
                .build();
    }
}
