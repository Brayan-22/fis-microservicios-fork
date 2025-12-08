package uni.fis.catalogo.Model.ItemDto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ProductoResponse {
    private Integer id;
    private Integer idCatalogo;
    private String nombre;
    private BigDecimal precio;
    private Date fechaCreacion;
    private BigDecimal valoración;
    private boolean disponible;
    private Integer cantidad;
    private String tamaño;
    private BigDecimal peso;
    private Integer id_color;
    private Integer id_unidad_peso;
}
