package tpu.ru.labor.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpu.ru.labor.exchange.entity.ProfileRole;

@Repository
public interface RoleRepository extends JpaRepository<ProfileRole, String> {

}
