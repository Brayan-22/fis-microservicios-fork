package FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoRequestDTO {
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private String tamaño;
    private String peso;
    private Integer id_color;
    private Integer id_unidad_peso;
}
