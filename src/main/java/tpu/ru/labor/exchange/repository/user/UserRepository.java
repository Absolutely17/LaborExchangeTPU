package tpu.ru.labor.exchange.repository.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tpu.ru.labor.exchange.entity.user.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @EntityGraph(value = "graph.Profile.roles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);

    int countByEmail(String email);

}
