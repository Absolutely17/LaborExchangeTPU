package tpu.ru.labor.exchange.security;

import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import tpu.ru.labor.exchange.entity.user.User;
import tpu.ru.labor.exchange.repository.user.UserRepository;

import java.util.Optional;

/**
 * Сервис по созданию CustomUserDetails
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Nullable
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return user.map(CustomUserDetails::new).orElse(null);
    }
}
