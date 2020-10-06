package tpu.ru.labor.exchange.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;
import tpu.ru.labor.exchange.dto.auth.Token;
import tpu.ru.labor.exchange.utils.CookieUtil;

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

    private static final String AUTHORIZATION = "Authorization";

    private static final long CONVERT_TO_SECONDS = 24L * 60L * 60L;

    private final CookieUtil cookieUtil;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessToken.expiration:7}")
    private long expAccessToken;

    @Value("${jwt.refreshToken.expiration:15}")
    private long expRefreshToken;

    public JwtProvider(CookieUtil cookieUtil) {
        this.cookieUtil = cookieUtil;
    }

    /**
     * Формируем токен доступа
     *
     * @param email адрес эл. почты для которого нужно сделать токен
     * @return токен
     */
    public Token generateAccessToken(String email) {
        log.info("Starting generate access token, email {}, expiration time {}d", email, expAccessToken);
        Date date = Date.from(LocalDate.now().plusDays(expAccessToken)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        String tokenValue = Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .setIssuedAt(new Date())
                .signWith(HS512, jwtSecret)
                .compact();
        return new Token(tokenValue, expAccessToken * CONVERT_TO_SECONDS);
    }

    /**
     * Формируем рефреш токен
     *
     * @param email адрес эл. почты для которого нужно сделать токен
     * @return токен
     */
    public Token generateRefreshToken(String email) {
        log.info("Starting generate refresh token, email {}, expiration time {}d", email, expRefreshToken);
        Date date = Date.from(LocalDate.now().plusDays(expRefreshToken)
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        String secret = KeyGenerators.string().generateKey();
        String tokenValue = Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .setIssuedAt(new Date())
                .claim("secret", secret)
                .signWith(HS512, jwtSecret)
                .compact();
        return new Token(tokenValue, expRefreshToken * CONVERT_TO_SECONDS, secret);
    }

    /**
     * Проверяем токен на валидность
     *
     * @param token токен, который нужно проверить
     * @return true - валидный, false - не валидный
     */
    boolean isValidToken(String token) {
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

    /**
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
     * Достаем токен из куки
     *
     * @param servletRequest запрос
     * @return токен
     */
    @Nullable
    String getTokenFromCookie(HttpServletRequest servletRequest) {
        log.debug("Getting token from cookie.");
        String accessToken = cookieUtil.getTokenFromCookie(servletRequest.getCookies());
        if (accessToken == null) {
            log.error("Token does not found in cookie request.");
            return null;
        } else {
            return accessToken;
        }
    }

    /**
     * Достаем эл. почту из токена
     *
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
