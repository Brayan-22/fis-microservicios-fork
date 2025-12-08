package uni.fis.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.OrdenCompra;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    void deleteByIdOrdenItem(Integer idOrdenItem);
    OrdenCompra[] findByIdPago(Integer idPago);
}
