package uni.fis.vehiculo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uni.fis.vehiculo.entities.VehiculoEntity;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<VehiculoEntity, Integer> {
    List<VehiculoEntity> findByIdUsuario(Integer idUsuario);
}
