package FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CatalogoDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer proveedorId;
    private Integer categoriaId;
}