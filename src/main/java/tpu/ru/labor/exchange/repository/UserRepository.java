package tpu.ru.labor.exchange.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tpu.ru.labor.exchange.entity.Profile;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Profile, String> {

    @EntityGraph(value = "graph.Profile.roles", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Profile> findByEmail(String email);

    Optional<Profile> findByEmailAndPassword(String email, String password);

    int countByEmail(String email);

}
