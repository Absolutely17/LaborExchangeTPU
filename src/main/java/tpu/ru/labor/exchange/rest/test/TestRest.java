package tpu.ru.labor.exchange.rest.test;

import org.springframework.web.bind.annotation.*;
import tpu.ru.labor.exchange.entity.user.User;
import tpu.ru.labor.exchange.repository.user.UserRepository;
import tpu.ru.labor.exchange.security.jwt.JwtProvider;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/test")
public class TestRest {

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    public TestRest(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @GetMapping("/permission")
    public String checkPermissions(
            @CookieValue(name = "accessToken") String token
    ) {
        User user = userRepository.findByEmail(jwtProvider.getEmailFromToken(token)).orElse(null);
        if (user == null) {
            return "You have permission. But your account does not found.";
        } else {
            return "You have permission. \n" + user.toString();
        }
    }

}
