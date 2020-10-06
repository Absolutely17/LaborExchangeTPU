package tpu.ru.labor.exchange.rest.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tpu.ru.labor.exchange.dto.auth.request.*;
import tpu.ru.labor.exchange.dto.user.response.ProfileResponseDto;
import tpu.ru.labor.exchange.service.AuthService;

/**
 * Created by SuhorukovIO on 04.10.2020
 * РЕСТ по аутентификации/регистрации пользователя
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
public class AuthRest {

    private final AuthService authService;

    public AuthRest(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Аутентификация пользователя
     *
     * @param requestDto данные для аутентификации
     * @return ответ
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthRequestDto requestDto
    ) {
        return authService.login(requestDto);
    }

    /**
     * Регистрация пользователя
     *
     * @param requestDto данные для регистрации
     * @return краткое ДТО с данными пользователя
     */
    @PostMapping("/register")
    public ProfileResponseDto register(
            @RequestBody RegisterRequestDto requestDto
    ) {
        return authService.register(requestDto);
    }
}
