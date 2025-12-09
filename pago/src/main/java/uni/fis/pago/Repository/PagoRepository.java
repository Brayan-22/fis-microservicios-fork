package uni.fis.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    Pago findByIdUsuario(Integer idUsuario);
}
