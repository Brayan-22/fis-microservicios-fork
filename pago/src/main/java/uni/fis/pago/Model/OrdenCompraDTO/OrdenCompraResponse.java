package uni.fis.pago.Model.OrdenCompraDTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrdenCompraResponse {
    private Integer id;
    private Date fecha;
    private Integer idPago;
    private Integer idOrdenItem;
}
