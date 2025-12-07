package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private LocalDate fechaNacimiento;
    private long id_documento;
    private String email;
    private int strikes;
    private Integer id_rol;
}
