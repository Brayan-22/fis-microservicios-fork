package uni.fis.catalogo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.catalogo.Entity.Catalogo;

@Repository
public interface CatalogoRepository extends JpaRepository<Catalogo, Integer> {
}