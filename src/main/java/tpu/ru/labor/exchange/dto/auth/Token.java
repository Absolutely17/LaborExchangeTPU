package tpu.ru.labor.exchange.dto.auth;

import lombok.*;

/**
 * Created by SuhorukovIO on 04.10.2020
 * Модель токена
 */
@Data
@AllArgsConstructor
public class Token {

    private String tokenValue;

    private Long duration;

    /**
     * Специфичное поле для рефреш токена
     */
    private String secret;

    public Token(String tokenValue, Long duration) {
        this.tokenValue = tokenValue;
        this.duration = duration;
    }
}
