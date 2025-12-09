package com.Rolapet.Noticia.repository;

import com.Rolapet.Noticia.domain.entity.Publicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    @Query("SELECT p FROM Publicacion p WHERE p.fecha <= :ahora " +
            "AND p.titulo LIKE '[NOTICIA]%' " +
            "ORDER BY p.fecha DESC")
    List<Publicacion> findPublicadas(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT p FROM Publicacion p WHERE p.fecha > :ahora " +
            "AND p.titulo LIKE '[NOTICIA]%' " +
            "ORDER BY p.fecha DESC")
    List<Publicacion> findBorradores(@Param("ahora") LocalDateTime ahora);

    @Query("SELECT p FROM Publicacion p WHERE p.titulo LIKE '[NOTICIA]%' " +
            "ORDER BY p.fecha DESC")
    List<Publicacion> findAllNoticias();

    @Query("SELECT p FROM Publicacion p WHERE p.id = :id AND p.titulo LIKE '[NOTICIA]%'")
    Optional<Publicacion> findNoticiaById(@Param("id") Integer id);
}
