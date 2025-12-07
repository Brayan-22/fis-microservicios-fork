package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProveedorDTO {
    private Integer id;
    private Integer idUsuario;
    private String nombre;
    private String correoContacto;
    private String categoria;
    private Boolean activo;
    private Integer tipoProveedorId;
}
