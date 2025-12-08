package uni.fis.contenido.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.contenido.entity.ContenidoEntity;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Integer> {
    List<ContenidoEntity> findByFechaCreacion(LocalDateTime fechaCreacion);
    List<ContenidoEntity> findByIdAutor(Integer usuario);
    Optional<ContenidoEntity> findById(Integer id);
}