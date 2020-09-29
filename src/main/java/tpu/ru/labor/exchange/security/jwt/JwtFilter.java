package tpu.ru.labor.exchange.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import tpu.ru.labor.exchange.security.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

private final JwtProvider jwtProvider;

    private final CustomUserDetailsService customUserDetailsService;

    public JwtFilter(
            JwtProvider jwtProvider,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtProvider = jwtProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String token = jwtProvider.getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            String userEmail = jwtProvider.getEmailFromToken(token);
            if (userEmail != null && !userEmail.isBlank()) {
                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userEmail);
                if (customUserDetails != null) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null,
                            customUserDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);
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
