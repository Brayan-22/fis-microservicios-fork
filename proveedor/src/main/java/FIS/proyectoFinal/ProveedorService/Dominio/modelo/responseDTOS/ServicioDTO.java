package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServicioDTO {
    private Integer id;
    private Integer idCatalogo;
    private String nombre;
    private BigDecimal precio;
    private Date fechaCreacion;
    private BigDecimal valoración;
    private boolean disponible;
    private String duracion;
    private LocalTime horario;
}
