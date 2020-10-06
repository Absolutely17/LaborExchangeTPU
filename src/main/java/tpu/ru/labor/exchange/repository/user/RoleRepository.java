package tpu.ru.labor.exchange.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tpu.ru.labor.exchange.entity.user.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, String> {

}
