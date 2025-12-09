package uni.fis.pago.Model.PagoDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class PagoRequest {
    private Integer id_usuario;
    private Integer id_metodo_pago;
}
