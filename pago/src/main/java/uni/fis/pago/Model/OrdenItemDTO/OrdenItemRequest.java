package uni.fis.pago.Model.OrdenItemDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdenItemRequest{
    private Integer id_item;
    private Integer cantidad;
}