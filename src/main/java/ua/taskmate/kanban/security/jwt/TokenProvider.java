package ua.taskmate.kanban.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ua.taskmate.kanban.dto.AuthenticationRequest;
import ua.taskmate.kanban.entity.User;
import ua.taskmate.kanban.security.UserDetailsImpl;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

@Component
public class TokenProvider {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;
    private static final String TOKEN_START = "Bearer_";

    public boolean validateToken(String token) {
        return !Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expired = new Date(now.getTime() + TimeUnit.HOURS.toMillis(1));
        return Jwts.builder()
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
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
        String userId = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        UserDetails userDetails = new UserDetailsImpl(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
