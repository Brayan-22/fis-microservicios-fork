package uni.fis.catalogo.Model.CatalogoDto;

import lombok.Data;

@Data
public class CatalogoRequest{
    private String nombre;
    private String descripcion;
    private Integer idCategoria;
    private Integer idProveedor;
}