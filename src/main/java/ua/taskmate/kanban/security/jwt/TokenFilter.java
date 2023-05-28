package ua.taskmate.kanban.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ua.taskmate.kanban.dto.RestExceptionDto;
import ua.taskmate.kanban.exception.JwtTokenProcessingException;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        Optional<String> token = tokenProvider.resolveToken(request);
        try {
            if (token.isPresent()) {
                tokenProvider.validateToken(token.get());
                Authentication authentication = tokenProvider.authentication(token.get());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (JwtTokenProcessingException e) {
            handleException(response, e);
        }
    }

    private void handleException(HttpServletResponse response, JwtTokenProcessingException e) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(e.getHttpStatus().value());
        response.getWriter().write(toJson(e));
    }

    private String toJson(JwtTokenProcessingException e) {
        try {
            RestExceptionDto restExceptionDto = RestExceptionDto.builder()
                    .payload(e.getReason())
                    .build();
            return objectMapper.writeValueAsString(restExceptionDto);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return e.getMessage();
    }
}
