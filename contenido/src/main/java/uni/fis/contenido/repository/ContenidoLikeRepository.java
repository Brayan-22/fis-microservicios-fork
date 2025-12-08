package uni.fis.contenido.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.contenido.entity.ContenidoLikeEntity;

public interface ContenidoLikeRepository extends JpaRepository<ContenidoLikeEntity, Integer> {
    boolean existsByContenidoIdAndIdUsuario(Integer idContenido, Integer idUsuario);
    void deleteByContenidoIdAndIdUsuario(Integer idContenido, Integer idUsuario);
    int countByContenidoId(Integer idContenido);
}
