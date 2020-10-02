package tpu.ru.labor.exchange.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "PROFILE_ROLE")
@Getter
@NoArgsConstructor
public class UserRole {

    @Id
    @Column(name = "ID")
    private String id;

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID", nullable = false)
    private User user;

    @Column(name = "ROLE")
    private String role;

    public UserRole(User user, String role) {
        id = UUID.randomUUID().toString();
        this.user = user;
        this.role = role;
    }

}
