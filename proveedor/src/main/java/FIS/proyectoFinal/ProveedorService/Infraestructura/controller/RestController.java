package FIS.proyectoFinal.ProveedorService.Infraestructura.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import FIS.proyectoFinal.ProveedorService.Aplicacion.service.ProveedorService;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.DarBajoServicioRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarCatalogoRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarComentarioRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarProductoRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarServicioRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.CatalogoDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ComentarioDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ProductoDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ProveedorDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ServicioDTO;

@RequestMapping("/api/proveedores")
@org.springframework.web.bind.annotation.RestController
public class RestController {
    private final ProveedorService proveedorService;
    public RestController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @PostMapping("/servicios")
    public ResponseEntity<ServicioDTO> publicarServicio(@RequestBody PublicarServicioRequestDTO body) {
        return ResponseEntity.ok(proveedorService.publicarServicio(body));
    }
    @PostMapping("/catalogos")
    public ResponseEntity<CatalogoDTO> publicarCatalogo(@RequestBody PublicarCatalogoRequestDTO body) {
        return ResponseEntity.ok(proveedorService.publicarCatalogo(body));
    }
    @PostMapping("/productos")
    public ResponseEntity<ProductoDTO> publicarProducto(@RequestBody PublicarProductoRequestDTO body) {
        return ResponseEntity.ok(proveedorService.publicarProducto(body));
    }
    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioDTO> publicarComentario(@RequestBody PublicarComentarioRequestDTO body) {
        return ResponseEntity.ok(proveedorService.publicarComentario(body));
    }
    @DeleteMapping("/servicios/{servicioId}")
    public ResponseEntity<ServicioDTO> darBajaServicio(@PathVariable Integer servicioId,
                                                       @RequestBody(required = false) DarBajoServicioRequestDTO body) {
        DarBajoServicioRequestDTO req = body != null ? body : new DarBajoServicioRequestDTO();
        req.setServicioId(servicioId);
        return ResponseEntity.ok(proveedorService.darDeBajaServicio(req));
    }
    @GetMapping("/{proveedorId}")
    public ResponseEntity<ProveedorDTO> consultarProveedor(@PathVariable Integer proveedorId) {
        return ResponseEntity.ok(proveedorService.consultarProveedor(proveedorId));
    }
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> consultarUsuarioRemoto(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(proveedorService.consultarUsuarioRemoto(usuarioId));
    }
}
