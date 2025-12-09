package com.RolaPet.Geolocalizacion.repository;
import com.RolaPet.Geolocalizacion.domain.entity.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DireccionRepository extends JpaRepository<Direccion, Integer> {

    List<Direccion> findByViaPrincipalContainingIgnoreCase(String viaPrincipal);
    List<Direccion> findByComplementoContainingIgnoreCase(String complemento);
    @Query("""
        SELECT d FROM Direccion d
        WHERE d.viaPrincipal = :viaPrincipal
        AND d.numeroVia = :numeroVia
        AND d.numeroUno = :numeroUno
        AND d.numeroDos = :numeroDos
        """)
    Optional<Direccion> findByComponentesExactos(
            @Param("viaPrincipal") String viaPrincipal,
            @Param("numeroVia") String numeroVia,
            @Param("numeroUno") String numeroUno,
            @Param("numeroDos") String numeroDos
    );
    @Query("""
        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
        FROM Direccion d
        WHERE LOWER(d.viaPrincipal) = LOWER(:viaPrincipal)
        AND LOWER(d.numeroVia) = LOWER(:numeroVia)
        AND LOWER(COALESCE(d.numeroUno, '')) = LOWER(COALESCE(:numeroUno, ''))
        AND LOWER(COALESCE(d.numeroDos, '')) = LOWER(COALESCE(:numeroDos, ''))
        AND LOWER(COALESCE(d.complemento, '')) = LOWER(COALESCE(:complemento, ''))
        """)
    boolean existsByComponentes(
            @Param("viaPrincipal") String viaPrincipal,
            @Param("numeroVia") String numeroVia,
            @Param("numeroUno") String numeroUno,
            @Param("numeroDos") String numeroDos,
            @Param("complemento") String complemento
    );
}
