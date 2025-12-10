package uni.fis.catalogo.Service;

import java.math.BigDecimal;

import uni.fis.catalogo.Model.ItemDto.*;

public interface ItemService {
    ProductoResponse agregarProducto(ProductoRequest ItemRequest, Integer catalogoId);
    ServicioResponse agregarServicio(ServicioRequest ItemRequest, Integer catalogoId);
    ProductoResponse obtenerProductoPorId(Integer id);
    ServicioResponse obtenerServicioPorId(Integer id);
    void eliminarProductoPorId(Integer id);
    void eliminarServicioPorId(Integer id);
    void calificarProducto(Integer id, BigDecimal calificacion);
    void calificarServicio(Integer id, BigDecimal calificacion);
    ProductoResponse[] obtenerTodosLosProductos();
    ServicioResponse[] obtenerTodosLosServicios();
}