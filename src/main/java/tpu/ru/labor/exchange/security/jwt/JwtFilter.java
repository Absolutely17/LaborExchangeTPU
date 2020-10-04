package tpu.ru.labor.exchange.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tpu.ru.labor.exchange.security.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Created by SuhorukovIO on 02.10.2020
 * Фильтр для доступа к ресурсам
 */
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

private final JwtProvider jwtProvider;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(
            JwtProvider jwtProvider,
            CustomUserDetailsService customUserDetailsService
    ) {
        super();
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException, ServletException {
        String token = jwtProvider.getTokenFromCookie(request);
        if (token != null && jwtProvider.isValidToken(token)) {
            String userEmail = jwtProvider.getEmailFromToken(token);
            if (userEmail != null && !userEmail.isBlank()) {
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userEmail);
                if (customUserDetails != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null,
                            customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    log.error("Could not find such user in the database");
                }
            } else {
                log.error("Email is empty or null. Auth failed.");
            }
        } else {
            log.error("Token does not exist in request or not valid.");
        }
        filterChain.doFilter(request, response);
    }

}
