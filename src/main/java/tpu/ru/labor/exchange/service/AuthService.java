package tpu.ru.labor.exchange.service;

import org.springframework.http.ResponseEntity;
import tpu.ru.labor.exchange.dao.request.*;
import tpu.ru.labor.exchange.dao.response.ProfileResponseDto;

/**
 * Created by SuhorukovIO on 04.10.2020
 * Сервис по аутентификации/регистрации пользователя
 */
public interface AuthService {

    /**
     * Аутентификация пользователя
     *
     * @param requestDto данные для входа
     * @return ответ, успешен ли вход
     */
    ResponseEntity<?> login(AuthRequestDto requestDto);

    /**
     * Регистрация пользователя
     *
     * @param requestDto данные для регистрации
     * @return ответ, успешна ли регистрация
     */
    ProfileResponseDto register(RegisterRequestDto requestDto);
}
