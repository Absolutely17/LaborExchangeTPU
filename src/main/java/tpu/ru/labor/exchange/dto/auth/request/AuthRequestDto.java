package tpu.ru.labor.exchange.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthRequestDto {

    private String email;

    private String password;

    private boolean isRemember;

    @JsonProperty("isRemember")
    public boolean isRemember() {
        return isRemember;
    }

    @Override
    public String toString() {
        return "AuthRequestDto{" +
                "email='" + email + '\'' +
                '}';
    }
}
