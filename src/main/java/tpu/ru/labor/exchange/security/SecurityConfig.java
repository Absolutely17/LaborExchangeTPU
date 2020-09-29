package tpu.ru.labor.exchange.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import tpu.ru.labor.exchange.security.jwt.JwtConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ROOT_PATH = "/api/v1";

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        super();
        this.jwtConfigurer = jwtConfigurer;
    }

    private final JwtConfigurer jwtConfigurer;

    @Override
    public void configure(WebSecurity web) {
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
