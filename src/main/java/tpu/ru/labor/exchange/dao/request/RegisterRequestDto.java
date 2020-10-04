package tpu.ru.labor.exchange.dao.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String email;

    private String password;

    private String firstName;

    private String middleName;

    private String secondName;

    private String sex;

    @Override
    public String toString() {
        return "RegisterRequestDto{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}
