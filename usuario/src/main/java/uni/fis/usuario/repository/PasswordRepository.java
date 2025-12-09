package uni.fis.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uni.fis.usuario.entity.PasswordEntity;

import java.util.Optional;

public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {

    @Query("SELECT p FROM PasswordEntity p WHERE p.idUsuario = :idUsuario ORDER BY p.fecha DESC LIMIT 1")
    Optional<PasswordEntity> findMasRecienteById(@Param("idUsuario") Integer idUsuario);
}