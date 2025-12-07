package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicarCatalogoRequestDTO {
    private String nombre;
    private String descripcion;
    private Integer proveedorId;
    private Integer categoriaId;
}
