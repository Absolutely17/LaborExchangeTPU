package tpu.ru.labor.exchange.dao.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequestDto {

    private String email;

    private String password;

    @Override
    public String toString() {
        return "AuthRequestDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
