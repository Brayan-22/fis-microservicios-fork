package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import java.math.BigDecimal;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicarServicioRequestDTO {
    private Integer catalogoId;
    private String nombre;
    private BigDecimal precio;
    private String duracion;
    private LocalTime horario;
}
