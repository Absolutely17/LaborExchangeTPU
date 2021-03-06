package tpu.ru.labor.exchange.service.impl.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tpu.ru.labor.exchange.dto.auth.Token;
import tpu.ru.labor.exchange.dto.auth.request.*;
import tpu.ru.labor.exchange.dto.user.response.ProfileResponseDto;
import tpu.ru.labor.exchange.entity.user.*;
import tpu.ru.labor.exchange.mapper.UserMapper;
import tpu.ru.labor.exchange.repository.user.*;
import tpu.ru.labor.exchange.security.jwt.JwtProvider;
import tpu.ru.labor.exchange.service.AuthService;
import tpu.ru.labor.exchange.utils.CookieUtil;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final JwtProvider jwtProvider;

    private final PasswordEncoder passwordEncoder;

    private final CookieUtil cookieUtil;

    private final UserMapper userMapper;

    public AuthServiceImpl(
            UserRepository userRepository,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            CookieUtil cookieUtil,
            UserMapper userMapper
    ) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cookieUtil = cookieUtil;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<?> login(AuthRequestDto requestDto) {
        log.debug("Login in system. {}", requestDto.toString());
        boolean isSuccessLogin = findByEmailAndPassword(requestDto.getEmail(), requestDto.getPassword());
        if (isSuccessLogin) {
            log.debug("Login success {}.", requestDto.getEmail());
            Token accessToken = jwtProvider.generateAccessToken(requestDto.getEmail());
            HttpHeaders responseHeaders = new HttpHeaders();
            if (requestDto.isRemember()) {
                Token refreshToken = jwtProvider.generateRefreshToken(requestDto.getEmail());
                responseHeaders.add(
                        SET_COOKIE,
                        cookieUtil.createRefreshTokenCookie(
                                refreshToken.getTokenValue(),
                                refreshToken.getDuration()
                        ).toString()
                );
            }
            responseHeaders.add(
                    SET_COOKIE,
                    cookieUtil.createAccessTokenCookie(
                            accessToken.getTokenValue(),
                            accessToken.getDuration()
                    ).toString()
            );
            return ResponseEntity.ok().headers(responseHeaders).build();
        } else {
            throw new IllegalArgumentException("Could not find the user with the specified email");
        }
    }

    private boolean findByEmailAndPassword(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Transactional
    @Override
    public ProfileResponseDto register(RegisterRequestDto requestDto) {
        log.debug("Register in system.", requestDto.toString());
        if (isAvailableEmail(requestDto.getEmail())) {
            log.debug("Email available. Continue register new user.");
            User user = userMapper.convertToUserFromRegisterRequest(requestDto);
            user = userRepository.save(user);
            UserRole role = new UserRole(user, "ROLE_USER");
            roleRepository.save(role);
            return userMapper.convertToProfileResponse(user);
        } else {
            log.error("Email is not available.");
            throw new IllegalArgumentException("Email is not available.");
        }
    }

    /**
     * Проверяем занят ли указанный адрес эл. почты, вытащив по данному адресу кол-во записей в БД
     *
     * @param email указанный адрес эл.почты
     * @return true - записей не найдено
     */
    private boolean isAvailableEmail(String email) {
        return userRepository.countByEmail(email) == 0;
    }
}
