package uni.fis.catalogo.Model.CatalogoDto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CatalogoResponse{
    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer idCategoria;
    private Integer idProveedor;
}