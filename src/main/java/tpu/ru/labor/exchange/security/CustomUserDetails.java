package tpu.ru.labor.exchange.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tpu.ru.labor.exchange.entity.Profile;

import java.util.*;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class CustomUserDetails implements UserDetails {

    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> grantedAuthorities;


    public CustomUserDetails(String email, String password, String role) {
        this.email = email;
        this.password = password;
        grantedAuthorities = singletonList(new SimpleGrantedAuthority(role));
    }

    public CustomUserDetails(Profile profile) {
        email = profile.getEmail();
        password = profile.getPassword();
        grantedAuthorities = profile.getRoles()
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
