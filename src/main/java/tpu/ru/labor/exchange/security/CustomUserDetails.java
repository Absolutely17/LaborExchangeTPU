package tpu.ru.labor.exchange.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tpu.ru.labor.exchange.entity.user.User;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Реализация UserDetails, которая нужна для аутентификации
 */
public class CustomUserDetails implements UserDetails {

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> grantedAuthorities;

    CustomUserDetails(User user) {
        email = user.getEmail();
        password = user.getPassword();
        grantedAuthorities = user.getRoles()
                .stream()
                .map(it -> new SimpleGrantedAuthority(it.getRole()))
                .collect(toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
