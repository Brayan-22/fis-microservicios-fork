package uni.fis.catalogo.Service;

import uni.fis.catalogo.Model.CatalogoDto.CatalogoRequest;
import uni.fis.catalogo.Model.CatalogoDto.CatalogoResponse;
import uni.fis.catalogo.Model.ItemDto.ProductoResponse;
import uni.fis.catalogo.Model.ItemDto.ServicioResponse;


public interface CatalogoService {
    long crearCatalogo(CatalogoRequest catalogoRequest);
    CatalogoResponse obtenerCatalogoPorId(Integer id);
    void eliminarCatalogoPorId(Integer id);
    ProductoResponse[] obtenerProductosPorCatalogoId(Integer catalogoId);
    ServicioResponse[] obtenerServiciosPorCatalogoId(Integer catalogoId);
}