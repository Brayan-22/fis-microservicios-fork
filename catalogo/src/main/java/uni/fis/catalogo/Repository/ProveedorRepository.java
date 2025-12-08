package uni.fis.catalogo.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import uni.fis.catalogo.Entity.ProveedorEntity;

@Repository
public interface ProveedorRepository extends CrudRepository<ProveedorEntity, Integer> {

    @Query(value = "SELECT id FROM proveedor WHERE id_usuario = :idUsuario LIMIT 1", nativeQuery = true)
    Integer findIdByIdUsuario(@Param("idUsuario") Integer idUsuario);
}
