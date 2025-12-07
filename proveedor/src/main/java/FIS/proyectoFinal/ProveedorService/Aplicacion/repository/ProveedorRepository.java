package FIS.proyectoFinal.ProveedorService.Aplicacion.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import FIS.proyectoFinal.ProveedorService.Dominio.entidad.ProveedorEntity;

public interface ProveedorRepository extends JpaRepository<ProveedorEntity, Integer> {

    List<ProveedorEntity> findByTipoProveedorId(Integer tipoId);

    List<ProveedorEntity> findByIdUsuario(Integer idUsuario);
}

