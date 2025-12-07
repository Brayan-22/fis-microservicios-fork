package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO {
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
