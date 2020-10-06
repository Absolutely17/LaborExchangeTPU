package tpu.ru.labor.exchange.dto.auth.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthResponseDto {

    private boolean isSuccess;

    private String token;

    public AuthResponseDto(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

}
