package tpu.ru.labor.exchange.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PROFILE_ROLE")
@Getter
@NoArgsConstructor
public class ProfileRole {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID", nullable = false)
    private Profile profile;

    @Column(name = "ROLE")
    private String role;

    public ProfileRole(Profile profile, String role) {
        id = UUID.randomUUID().toString();
        this.profile = profile;
        this.role = role;
    }

}
