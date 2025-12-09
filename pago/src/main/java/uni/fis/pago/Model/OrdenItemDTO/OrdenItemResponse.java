package uni.fis.pago.Model.OrdenItemDTO;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenItemResponse{
    private Integer id;
    private Integer id_item;
    private Integer cantidad;
    private BigDecimal valor_unitario;
    private BigDecimal subtotal;
    private Integer id_orden_compra;
}