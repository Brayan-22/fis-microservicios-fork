package FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicioRequestDTO {
    private String nombre;
    private BigDecimal precio;
    private String duracion;
    private LocalTime horario;
}
