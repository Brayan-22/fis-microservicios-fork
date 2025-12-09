package uni.fis.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.usuario.entity.UsuarioEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UsuarioEntity, Integer> {
    boolean existsByEmail(String email);

    Optional<UsuarioEntity> findByEmail(String email);
}
