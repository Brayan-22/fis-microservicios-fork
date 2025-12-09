package com.Rolapet.Noticia.repository;

import com.Rolapet.Noticia.domain.entity.Contenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContenidoRepository extends JpaRepository<Contenido, Integer> {
}
