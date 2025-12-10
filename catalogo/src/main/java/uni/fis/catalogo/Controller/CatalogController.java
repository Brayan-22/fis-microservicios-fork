package uni.fis.catalogo.Controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.fis.catalogo.Model.CatalogoDto.CatalogoRequest;
import uni.fis.catalogo.Model.CatalogoDto.CatalogoResponse;
import uni.fis.catalogo.Model.ItemDto.ProductoRequest;
import uni.fis.catalogo.Model.ItemDto.ProductoResponse;
import uni.fis.catalogo.Model.ItemDto.ServicioRequest;
import uni.fis.catalogo.Model.ItemDto.ServicioResponse;
import uni.fis.catalogo.Service.CatalogoService;
import uni.fis.catalogo.Service.ItemService;

@RestController
@RequestMapping("api/catalogo")
public class CatalogController {
    
    @Autowired
    private CatalogoService catalogoService;
    
    @Autowired
    private ItemService itemService;

    @PostMapping("/crear")
    public ResponseEntity<Long> crearEntityCatalogo(@RequestBody CatalogoRequest catalogoRequest) {
        long catalogoId = catalogoService.crearCatalogo(catalogoRequest);
        return new ResponseEntity<>(catalogoId, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CatalogoResponse> obtenerCatalogoPorId(@PathVariable("id") Integer id) {
        CatalogoResponse catalogoResponse = catalogoService.obtenerCatalogoPorId(id);
        return new ResponseEntity<>(catalogoResponse, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}/eliminar")
    public ResponseEntity<String> eliminarCatalogoPorId(@PathVariable("id") Integer id) {
        catalogoService.eliminarCatalogoPorId(id);
        return new ResponseEntity<>("Catálogo eliminado exitosamente", HttpStatus.OK);
    }

    @GetMapping("/{catalogoId}/listaProductos")
    public ResponseEntity<ProductoResponse[]> obtenerProductosPorCatalogoId(
            @PathVariable("catalogoId") Integer catalogoId) {
        ProductoResponse[] productos = catalogoService.obtenerProductosPorCatalogoId(catalogoId);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
    
    @GetMapping("/{idCatalogo}/producto/{id}")
    public ResponseEntity<ProductoResponse> obtenerProductoPorId(
            @PathVariable("idCatalogo") Integer idCatalogo,
            @PathVariable("id") Integer id) {
        ProductoResponse productoResponse = itemService.obtenerProductoPorId(id);
        return new ResponseEntity<>(productoResponse, HttpStatus.OK);
    }
    
    @PostMapping("/{catalogoId}/producto")
    public ResponseEntity<ProductoResponse> agregarProductoAlCatalogo(
            @PathVariable("catalogoId") Integer catalogoId,
            @RequestBody ProductoRequest productoRequest) {
        System.out.println("cantidad recibida: " + productoRequest.getCantidad());
        ProductoResponse productoResponse = itemService.agregarProducto(productoRequest, catalogoId);
        return new ResponseEntity<>(productoResponse, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{idCatalogo}/producto/{id}/eliminar")
    public ResponseEntity<String> eliminarProductoPorId(
            @PathVariable("idCatalogo") Integer idCatalogo,
            @PathVariable("id") Integer id) {
        itemService.eliminarProductoPorId(id);
        return new ResponseEntity<>("Producto eliminado exitosamente", HttpStatus.OK);
    }
    
    @PostMapping("/producto/{id}/calificar")
    public ResponseEntity<String> calificarProducto(
            @PathVariable("id") Integer id, 
            @RequestBody BigDecimal calificacion) {
        itemService.calificarProducto(id, calificacion);
        return new ResponseEntity<>("Producto calificado exitosamente", HttpStatus.OK);
    }

    @GetMapping("/{catalogoId}/listaServicios")
    public ResponseEntity<ServicioResponse[]> obtenerServiciosPorCatalogoId(
            @PathVariable("catalogoId") Integer catalogoId) {
        ServicioResponse[] servicios = catalogoService.obtenerServiciosPorCatalogoId(catalogoId);
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }
    
    @GetMapping("/{idCatalogo}/servicio/{id}")
    public ResponseEntity<ServicioResponse> obtenerServicioPorId(
            @PathVariable("idCatalogo") Integer idCatalogo,
            @PathVariable("id") Integer id) {
        ServicioResponse servicioResponse = itemService.obtenerServicioPorId(id);
        return new ResponseEntity<>(servicioResponse, HttpStatus.OK);
    }
    
    @PostMapping("/{catalogoId}/servicio")
    public ResponseEntity<ServicioResponse> agregarServicioAlCatalogo(
            @PathVariable("catalogoId") Integer catalogoId,
            @RequestBody ServicioRequest servicioRequest) {
        ServicioResponse servicioResponse = itemService.agregarServicio(servicioRequest, catalogoId);
        return new ResponseEntity<>(servicioResponse, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{idCatalogo}/servicio/{id}/eliminar")
    public ResponseEntity<String> eliminarServicioPorId(
            @PathVariable("idCatalogo") Integer idCatalogo,
            @PathVariable("id") Integer id) {
        itemService.eliminarServicioPorId(id);
        return new ResponseEntity<>("Servicio eliminado exitosamente", HttpStatus.OK);
    }
    
    @PostMapping("/servicio/{id}/calificar")
    public ResponseEntity<String> calificarServicio(
            @PathVariable("id") Integer id, 
            @RequestBody BigDecimal calificacion) {
        itemService.calificarServicio(id, calificacion);
        return new ResponseEntity<>("Servicio calificado exitosamente", HttpStatus.OK);
    }
    @GetMapping("/catalogos")
    public ResponseEntity<CatalogoResponse[]> obtenerTodosLosCatalogos() {
        CatalogoResponse[] catalogos = catalogoService.obtenerTodosLosCatalogos();
        return new ResponseEntity<>(catalogos, HttpStatus.OK);
    }
    @GetMapping("/productos")
    public ResponseEntity<ProductoResponse[]> obtenerTodosLosProductos() {
        ProductoResponse[] productos = itemService.obtenerTodosLosProductos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }
    @GetMapping("/servicios")
    public ResponseEntity<ServicioResponse[]> obtenerTodosLosServicios() {
        ServicioResponse[] servicios = itemService.obtenerTodosLosServicios();
        return new ResponseEntity<>(servicios, HttpStatus.OK);
    }
}