package uni.fis.catalogo.Service.Imp;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.catalogo.Entity.Items.Producto;
import uni.fis.catalogo.Entity.Items.Servicio;
import uni.fis.catalogo.Exceptions.ItemNotFoundException;
import uni.fis.catalogo.Model.ItemDto.ProductoRequest;
import uni.fis.catalogo.Model.ItemDto.ProductoResponse;
import uni.fis.catalogo.Model.ItemDto.ServicioRequest;
import uni.fis.catalogo.Model.ItemDto.ServicioResponse;
import uni.fis.catalogo.Repository.ProductoRepository;
import uni.fis.catalogo.Repository.ServicioRepository;
import uni.fis.catalogo.Service.ItemService;

@Service
public class ItemServiceImp implements ItemService {
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public ProductoResponse agregarProducto(ProductoRequest productoRequest, Integer catalogoId) {
        Producto producto = Producto.builder()
                .idCatalogo(catalogoId)
                .nombre(productoRequest.getNombre())
                .precio(productoRequest.getPrecio())
                .fechaCreacion(new Date())
                .valoracion(new BigDecimal(0))
                .disponible(true)
                .idMultimedia(productoRequest.getIdMultimedia())
                .cantidad((int)productoRequest.getCantidad())
                .tamano(productoRequest.getTamaño())
                .peso(productoRequest.getPeso())
                .id_color(productoRequest.getId_color())
                .id_unidad_peso(productoRequest.getId_unidad_peso())
                .build();
        productoRepository.save(producto);
        return ProductoResponse.builder()
                .id(producto.getId())
                .idCatalogo(producto.getIdCatalogo())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .fechaCreacion(producto.getFechaCreacion())
                .valoración(producto.getValoracion())
                .disponible(producto.isDisponible())
                .idMultimedia(producto.getIdMultimedia())
                .cantidad(producto.getCantidad())
                .tamaño(producto.getTamano())
                .peso(producto.getPeso())
                .id_color(producto.getId_color())
                .id_unidad_peso(producto.getId_unidad_peso())
                .build();
    }
    
    @Override 
    public ProductoResponse obtenerProductoPorId(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Producto con el id "+ id + " no fue encontrado", "PRODUCTO_NOT_FOUND"));
        return ProductoResponse.builder()
                .id(producto.getId())
                .idCatalogo(producto.getIdCatalogo())
                .nombre(producto.getNombre())
                .precio(producto.getPrecio())
                .fechaCreacion(producto.getFechaCreacion())
                .valoración(producto.getValoracion())
                .disponible(producto.isDisponible())
                .idMultimedia(producto.getIdMultimedia())
                .cantidad(producto.getCantidad())
                .tamaño(producto.getTamano())
                .peso(producto.getPeso())
                .id_color(producto.getId_color())
                .id_unidad_peso(producto.getId_unidad_peso())
                .build();
    }
    
    @Override
    public void eliminarProductoPorId(Integer id) {
        if (!productoRepository.existsById(id)) {
            throw new ItemNotFoundException("Producto con el id " + id + " no fue encontrado", "PRODUCTO_NOT_FOUND");
        }
        productoRepository.deleteById(id);
    }
    
    @Override 
    public ServicioResponse agregarServicio(ServicioRequest servicioRequest, Integer catalogoId){
        Servicio servicio = Servicio.builder()
                .idCatalogo(catalogoId)
                .nombre(servicioRequest.getNombre())
                .precio(servicioRequest.getPrecio())
                .fechaCreacion(new Date())
                .valoracion(new BigDecimal(0))
                .disponible(true)
                .idMultimedia(servicioRequest.getIdMultimedia())
                .duracion(servicioRequest.getDuracion())
                .horario(servicioRequest.getHorario())
                .build();
        servicioRepository.save(servicio);
        return ServicioResponse.builder()
                .id(servicio.getId())
                .idCatalogo(servicio.getIdCatalogo())
                .nombre(servicio.getNombre())
                .precio(servicio.getPrecio())
                .fechaCreacion(servicio.getFechaCreacion())
                .valoración(servicio.getValoracion())
                .disponible(servicio.isDisponible())
                .idMultimedia(servicio.getIdMultimedia())
                .duracion(servicio.getDuracion())
                .horario(servicio.getHorario())
                .build();
    }
    
    @Override 
    public ServicioResponse obtenerServicioPorId(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Producto con el id "+ id + " no fue encontrado", "PRODUCTO_NOT_FOUND"));
        return ServicioResponse.builder()
                .id(servicio.getId())
                .idCatalogo(servicio.getIdCatalogo())
                .nombre(servicio.getNombre())
                .precio(servicio.getPrecio())
                .fechaCreacion(servicio.getFechaCreacion())
                .valoración(servicio.getValoracion())
                .disponible(servicio.isDisponible())
                .idMultimedia(servicio.getIdMultimedia())
                .duracion(servicio.getDuracion())
                .horario(servicio.getHorario())
                .build();
    }
    
    @Override 
    public void eliminarServicioPorId(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new ItemNotFoundException("Servicio con el id " + id + " no fue encontrado", "SERVICIO_NOT_FOUND");
        }
        servicioRepository.deleteById(id);
    }
    
    @Override
    public void calificarProducto(Integer id, BigDecimal calificacion) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Producto con el id "+ id + " no fue encontrado", "PRODUCTO_NOT_FOUND"));
        producto.setValoracion(calificacion);
        productoRepository.save(producto);
    }
    
    @Override
    public void calificarServicio(Integer id, BigDecimal calificacion) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Servicio con el id "+ id + " no fue encontrado", "SERVICIO_NOT_FOUND"));
        servicio.setValoracion(calificacion);
        servicioRepository.save(servicio);
    }
    @Override
    public ProductoResponse[] obtenerTodosLosProductos() {
        ProductoResponse[] productos = productoRepository.findAll().stream()
            .map(producto -> ProductoResponse.builder()
                    .id(producto.getId())
                    .idCatalogo(producto.getIdCatalogo())
                    .nombre(producto.getNombre())
                    .precio(producto.getPrecio())
                    .fechaCreacion(producto.getFechaCreacion())
                    .valoración(producto.getValoracion())
                    .disponible(producto.isDisponible())
                    .idMultimedia(producto.getIdMultimedia())
                    .cantidad(producto.getCantidad())
                    .tamaño(producto.getTamano())
                    .peso(producto.getPeso())
                    .id_color(producto.getId_color())
                    .id_unidad_peso(producto.getId_unidad_peso())
                    .build())
            .toArray(ProductoResponse[]::new);

        return productos;
    }
    @Override
    public ServicioResponse[] obtenerTodosLosServicios() {
        ServicioResponse[] servicios = servicioRepository.findAll().stream()
            .map(servicio -> ServicioResponse.builder()
                    .id(servicio.getId())
                    .idCatalogo(servicio.getIdCatalogo())
                    .nombre(servicio.getNombre())
                    .precio(servicio.getPrecio())
                    .fechaCreacion(servicio.getFechaCreacion())
                    .valoración(servicio.getValoracion())
                    .disponible(servicio.isDisponible())
                    .idMultimedia(servicio.getIdMultimedia())
                    .duracion(servicio.getDuracion())
                    .horario(servicio.getHorario())
                    .build())
            .toArray(ServicioResponse[]::new);

        return servicios;
    }
}