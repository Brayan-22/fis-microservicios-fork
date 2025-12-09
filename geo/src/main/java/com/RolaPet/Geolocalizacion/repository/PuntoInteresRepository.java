package com.RolaPet.Geolocalizacion.repository;
import com.RolaPet.Geolocalizacion.domain.entity.PuntoInteres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface PuntoInteresRepository extends JpaRepository<PuntoInteres, Integer> {

    List<PuntoInteres> findByNombreContainingIgnoreCase(String nombre);
    List<PuntoInteres> findByDescripcionContainingIgnoreCase(String descripcion);
    List<PuntoInteres> findByIdDireccionIsNotNull();
    List<PuntoInteres> findByIdDireccion(Integer idDireccion);
}
