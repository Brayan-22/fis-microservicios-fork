package uni.fis.contenido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.contenido.entity.PublicacionEntity;
import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionEntity, Integer> {

    List<PublicacionEntity> findByForo(Integer foro);
    List<PublicacionEntity> findByContenido_IdAutor(Integer idAutor);
}
