package tpu.ru.labor.exchange.dao.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String email;

    private String password;

    @Override
    public String toString() {
        return "RegisterRequestDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
