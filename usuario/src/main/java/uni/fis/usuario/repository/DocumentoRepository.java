package uni.fis.usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import uni.fis.usuario.entity.DocumentoEntity;

public interface DocumentoRepository extends JpaRepository<DocumentoEntity, Integer> {

}
