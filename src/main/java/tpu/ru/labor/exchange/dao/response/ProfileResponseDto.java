package tpu.ru.labor.exchange.dao.response;

import lombok.*;
import tpu.ru.labor.exchange.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileResponseDto {

    private String email;

    private String firstName;

    private String middleName;

    private String secondName;

    private String sex;

    public ProfileResponseDto(User user) {
        email = user.getEmail();
        firstName = user.getFirstName();
        middleName = user.getMiddleName();
        secondName = user.getSecondName();
        sex = user.getSex();
    }
}
