package tpu.ru.labor.exchange.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import tpu.ru.labor.exchange.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.*;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Component
public class JwtProvider {

    private static final SecureRandom random = new SecureRandom();

    private static final String ALL_SYMBOLS_TO_GENERATE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz/*&^$@!";

    private static final String AUTHORIZATION = "Authorization";

    @Value("$(jwt.secret)")
    private String jwtSecret;

    @Value("${jwt.expiration.access.token:7}")
    private long expAccessToken;

    public String generateAccessToken(String email) {
        log.info("Starting generate access token, email {}, expiration time {}d", email, expAccessToken);
        Date date = Date.from(LocalDate.now().plusDays(expAccessToken)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .claim("salt", generateRandomSalt())
                .signWith(HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired");
        } catch (UnsupportedJwtException unsEx) {
            log.error("Unsupported jwt");
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt");
        } catch (SignatureException sEx) {
            log.error("Invalid signature");
        } catch (Exception e) {
            log.error("Invalid token");
        }
        return false;
    }

    private static String generateRandomSalt() {
        StringBuilder sb = new StringBuilder();
        int lengthSalt = random.nextInt(15 - 10) + 10;
        for (int i = 0; i < lengthSalt; i++) {
            sb.append(ALL_SYMBOLS_TO_GENERATE.charAt(random.nextInt(ALL_SYMBOLS_TO_GENERATE.length())));
        }
        return sb.toString();
    }

    @Nullable
    public String getTokenFromRequest(HttpServletRequest servletRequest) {
        log.debug("Getting token from request...");
        String bearer = servletRequest.getHeader(AUTHORIZATION);
        if (hasText(bearer) && bearer.startsWith("Bearer")) {
            return bearer.substring(7);
        } else {
            return null;
        }
    }

    @Nullable
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

}
