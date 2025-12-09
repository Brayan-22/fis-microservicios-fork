package uni.fis.pago.Model.OrdenCompraDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdenCompraRequest {
    private Integer idPago;
    private Integer idOrdenItem;
}
