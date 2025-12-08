package uni.fis.foro.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uni.fis.foro.entity.ForoEntity;


@Repository
public interface ForoRepository extends JpaRepository<ForoEntity, Integer> {
    Optional<ForoEntity> findById(Integer id);
}