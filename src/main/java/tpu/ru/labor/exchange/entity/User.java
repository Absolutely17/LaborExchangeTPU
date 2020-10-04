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

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @Column(name = "SECOND_NAME")
    private String secondName;

    @Column(name = "SEX")
    private String sex;

    @OneToMany(mappedBy = "user")
    private List<UserRole> roles;

    public User(String email, String password, String firstName, String middleName, String secondName, String sex) {
        id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.secondName = secondName;
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
