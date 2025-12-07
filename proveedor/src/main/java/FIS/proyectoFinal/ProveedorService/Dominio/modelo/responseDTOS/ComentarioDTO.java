package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComentarioDTO {
    private Integer id;
    private LocalDateTime fecha;
    private String mensaje;
    private String tipo_objeto;
    private Integer id_objeto;
}
