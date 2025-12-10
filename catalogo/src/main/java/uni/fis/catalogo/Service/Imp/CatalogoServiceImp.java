package uni.fis.catalogo.Service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.catalogo.Entity.Catalogo;
import uni.fis.catalogo.Exceptions.CatalogoNotFoundException;
import uni.fis.catalogo.Model.CatalogoDto.CatalogoRequest;
import uni.fis.catalogo.Model.CatalogoDto.CatalogoResponse;
import uni.fis.catalogo.Model.ItemDto.ProductoResponse;
import uni.fis.catalogo.Model.ItemDto.ServicioResponse;
import uni.fis.catalogo.Repository.CatalogoRepository;
import uni.fis.catalogo.Repository.ProductoRepository;
import uni.fis.catalogo.Repository.ServicioRepository;
import uni.fis.catalogo.Service.CatalogoService;

@Service
public class CatalogoServiceImp implements CatalogoService {
    @Autowired
    private CatalogoRepository catalogoRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ServicioRepository servicioRepository;
    
    @Override
    public long crearCatalogo(CatalogoRequest catalogoRequest) {
        Catalogo catalogo = Catalogo.builder()
                .nombre(catalogoRequest.getNombre())
                .descripcion(catalogoRequest.getDescripcion())
                .idCategoria(catalogoRequest.getIdCategoria())
                .idProveedor(catalogoRequest.getIdProveedor())
                .build();
        catalogoRepository.save(catalogo);
        return catalogo.getId();
    }
    
    @Override 
    public CatalogoResponse obtenerCatalogoPorId(Integer id) {
        Catalogo catalogo = catalogoRepository.findById(id)
                .orElseThrow(() -> new CatalogoNotFoundException("Catálogo con el id "+ id + " no fue encontrado", "CATALOGO_NOT_FOUND"));
        
        return CatalogoResponse.builder()
                .id(catalogo.getId())
                .nombre(catalogo.getNombre())
                .descripcion(catalogo.getDescripcion())
                .idCategoria(catalogo.getIdCategoria())
                .idProveedor(catalogo.getIdProveedor())
                .build();
    }
    
    @Override
    public void eliminarCatalogoPorId(Integer id) {
        if (!catalogoRepository.existsById(id)) {
            throw new CatalogoNotFoundException("Catálogo con el id " + id + " no fue encontrado", "CATALOGO_NOT_FOUND");
        }
        catalogoRepository.deleteById(id);
    }
    
    @Override
    public ProductoResponse[] obtenerProductosPorCatalogoId(Integer catalogoId) {
        ProductoResponse[] productos = productoRepository.findByIdCatalogo(catalogoId).stream()
            .map(producto -> ProductoResponse.builder()
                    .id(producto.getId())
                    .nombre(producto.getNombre())
                    .precio(producto.getPrecio())
                    .cantidad(producto.getCantidad())
                    .idCatalogo(producto.getIdCatalogo())
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
    public ServicioResponse[] obtenerServiciosPorCatalogoId(Integer catalogoId) {
        ServicioResponse[] servicios = servicioRepository.findByIdCatalogo(catalogoId).stream()
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
    
    @Override
    public CatalogoResponse[] obtenerTodosLosCatalogos() {
        CatalogoResponse[] catalogos = catalogoRepository.findAll().stream()
            .map(catalogo -> CatalogoResponse.builder()
                    .id(catalogo.getId())
                    .nombre(catalogo.getNombre())
                    .descripcion(catalogo.getDescripcion())
                    .idCategoria(catalogo.getIdCategoria())
                    .idProveedor(catalogo.getIdProveedor())
                    .build())
            .toArray(CatalogoResponse[]::new);

        return catalogos;
    }
}