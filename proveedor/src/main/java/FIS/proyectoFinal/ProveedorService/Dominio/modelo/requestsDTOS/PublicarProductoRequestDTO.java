package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicarProductoRequestDTO {
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private String tamano;
    private BigDecimal peso;
    private Integer idColor;
    private Integer idUnidadPeso;
    private Integer catalogoId;
}
