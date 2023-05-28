package ua.taskmate.kanban.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.exception.JwtTokenProcessingException;
import ua.taskmate.kanban.security.UserDetailsImpl;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private Algorithm algorithm;
    private static final String TOKEN_START = "Bearer_";

    public void validateToken(String token) {
        boolean expired = JWT.decode(token)
                .getExpiresAt()
                .before(new Date());
        if (expired) {
            throw new JwtTokenProcessingException(HttpStatus.UNAUTHORIZED, "Token is expired!");
        }
    }

    public String generateToken(User user) {
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + TimeUnit.HOURS.toMillis(1));
        return JWT.create()
                .withClaim("userId", user.getId())
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiredAt)
                .sign(algorithm);
    }

    public Optional<String> resolveToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        String token = null;
        if (nonNull(authorizationHeader) && authorizationHeader.startsWith(TOKEN_START)) {
            token = authorizationHeader.substring(TOKEN_START.length());
        }

        return Optional.ofNullable(token);
    }

    public Authentication authentication(String token) {
        try {
            String userId = JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getClaim("userId")
                    .asString();
            UserDetails userDetails = new UserDetailsImpl(userId);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (JWTVerificationException e) {
            throw new JwtTokenProcessingException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Autowired
    public void setAlgorithm(@Lazy Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
