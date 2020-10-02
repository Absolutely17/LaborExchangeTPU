package tpu.ru.labor.exchange.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import tpu.ru.labor.exchange.security.jwt.JwtConfigurer;

/**
 * Конфигурация Spring Security
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        super();
        this.jwtConfigurer = jwtConfigurer;
    }

    private final JwtConfigurer jwtConfigurer;

    @Override
    public void configure(WebSecurity web) {
        // Убираем фильтрацию запросов по РЕСТам, связанным с профилем
        web.ignoring().antMatchers("/profile/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/test/permission").hasAnyRole("USER", "ADMIN")
                .and().apply(jwtConfigurer);
    }
}
