package uni.fis.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.usuario.entity.RolEntity;

public interface RolRepository extends JpaRepository<RolEntity,Integer> {
}
