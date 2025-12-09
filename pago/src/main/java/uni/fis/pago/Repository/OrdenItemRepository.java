package uni.fis.pago.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uni.fis.pago.Entity.OrdenItem;

@Repository
public interface OrdenItemRepository extends JpaRepository<OrdenItem, Integer> {
}
