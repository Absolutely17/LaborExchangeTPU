package tpu.ru.labor.exchange.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tpu.ru.labor.exchange.dao.request.*;
import tpu.ru.labor.exchange.dao.response.*;
import tpu.ru.labor.exchange.entity.*;
import tpu.ru.labor.exchange.repository.*;
import tpu.ru.labor.exchange.security.jwt.JwtProvider;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public AuthResponseDto login(AuthRequestDto requestDto) {
        log.debug("Login in system.", requestDto.toString());
        boolean isSuccessLogin = isSuccessLoginWithEmailAndPass(requestDto.getEmail(), requestDto.getPassword());
        if (isSuccessLogin) {
            log.debug("Login success.");
            String token = jwtProvider.generateAccessToken(requestDto.getEmail());
            return new AuthResponseDto(true, token);
        } else {
            log.error("Login failed.");
            return new AuthResponseDto(false);
        }
    }

    private boolean isSuccessLoginWithEmailAndPass(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    public ProfileResponseDto register(RegisterRequestDto requestDto) {
        log.debug("Register in system.", requestDto.toString());
        if (isAvailableEmail(requestDto.getEmail())) {
            log.debug("Email available. Continue register new user.");
            User user = new User(
                    requestDto.getEmail(),
                    passwordEncoder.encode(requestDto.getPassword())
            );
            user = userRepository.save(user);
            UserRole role = new UserRole(user, "ROLE_USER");
            roleRepository.save(role);
            return new ProfileResponseDto(user.getEmail());
        } else {
            log.error("Email is not available.");
            throw new IllegalStateException("Email is not available.");
        }
    }

    private boolean isAvailableEmail(String email) {
        return userRepository.countByEmail(email) == 0;
    }

}
