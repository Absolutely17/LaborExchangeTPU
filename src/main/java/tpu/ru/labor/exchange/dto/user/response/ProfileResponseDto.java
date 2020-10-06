package tpu.ru.labor.exchange.dto.user.response;

import lombok.*;

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
}
