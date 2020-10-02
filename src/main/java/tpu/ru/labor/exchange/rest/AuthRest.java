package tpu.ru.labor.exchange.rest;

import org.springframework.web.bind.annotation.*;
import tpu.ru.labor.exchange.dao.request.*;
import tpu.ru.labor.exchange.dao.response.*;
import tpu.ru.labor.exchange.service.AuthService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/user")
public class AuthRest {

    private final AuthService authService;

    public AuthRest(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponseDto login(
            @RequestBody AuthRequestDto requestDto
    ) {
        return authService.login(requestDto);
    }

    @PostMapping("/register")
    public ProfileResponseDto register(
            @RequestBody RegisterRequestDto requestDto
    ) {
        return authService.register(requestDto);
    }

}
