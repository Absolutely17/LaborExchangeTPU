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

/**
 * Created by SuhorukovIO on 02.10.2020
 * Сервис для обработки JWT токенов
 */
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

    /**
     * Формируем токен доступа
     * @param email адрес эл. почты для которого нужно сделать токен
     * @return токен
     */
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

    /**
     * Проверяем токен на валидность
     * @param token токен, который нужно проверить
     * @return true - валидный, false - не валидный
     */
    public boolean isValidToken(String token) {
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

    /**
     * Created by SuhorukovIO on 02.10.2020
     * Вытаскиваем токен из заголовка запроса
     * @param servletRequest запрос
     * @return токен
     */
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

    /**
     * Достаем эл. почту из токена
     * @param token токен из которого вытаскивать необходимо
     * @return эл. почта
     */
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
