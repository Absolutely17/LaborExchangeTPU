package tpu.ru.labor.exchange.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "PROFILE")
@NamedEntityGraph(name = "graph.Profile.roles", attributeNodes = @NamedAttributeNode("roles"))
@Getter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;

    public User(String email, String password) {
        id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
    }

}
