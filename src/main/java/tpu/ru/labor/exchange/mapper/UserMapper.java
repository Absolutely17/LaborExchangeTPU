package tpu.ru.labor.exchange.mapper;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tpu.ru.labor.exchange.dto.auth.request.RegisterRequestDto;
import tpu.ru.labor.exchange.dto.user.response.ProfileResponseDto;
import tpu.ru.labor.exchange.entity.user.User;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ProfileResponseDto convertToProfileResponse(User user) {
        return new ProfileResponseDto(
                user.getEmail(),
                user.getFirstName(),
                user.getMiddleName(),
                user.getSecondName(),
                user.getSex()
        );
    }

    public User convertToUserFromRegisterRequest(RegisterRequestDto requestDto) {
        return new User(
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                requestDto.getFirstName(),
                requestDto.getMiddleName(),
                requestDto.getSecondName(),
                requestDto.getSex()
        );
    }
}
