package FIS.proyectoFinal.ProveedorService.Aplicacion.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import FIS.proyectoFinal.ProveedorService.Aplicacion.repository.ProveedorRepository;
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
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.UsuarioDTO;
import FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate.CatalogClient;
import FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate.UserClient;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final UserClient userClient;
    private final CatalogClient catalogClient;

    public ProveedorService(ProveedorRepository proveedorRepository, UserClient userClient, CatalogClient catalogClient){
        this.proveedorRepository = proveedorRepository;
        this.userClient = userClient;
        this.catalogClient = catalogClient;
    }

    public ServicioDTO publicarServicio(PublicarServicioRequestDTO req){
        return catalogClient.publicarServicio(req);
    }
    public ProductoDTO publicarProducto(PublicarProductoRequestDTO req) {
        return catalogClient.publicarProducto(req);
    }

    public CatalogoDTO publicarCatalogo(PublicarCatalogoRequestDTO req){
        Long catalogoId = catalogClient.publicarCatalogo(req);

        CatalogoDTO dto = new CatalogoDTO();
        dto.setId(catalogoId != null ? catalogoId.intValue() : null);
        dto.setProveedorId(req.getProveedorId() != null ? req.getProveedorId().intValue() : null);
        dto.setNombre(req.getNombre());
        dto.setDescripcion(req.getDescripcion());
        dto.setCategoriaId(req.getCategoriaId());
        return dto;
    }

    public ComentarioDTO publicarComentario(PublicarComentarioRequestDTO req){
        return catalogClient.publicarComentario(req);
    }

    public ServicioDTO darDeBajaServicio(DarBajoServicioRequestDTO req){
        return catalogClient.darDeBajaServicio(req);
    }

    public ProveedorDTO consultarProveedor(Integer proveedorId){
        if (proveedorId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "proveedorId is required");
        } else {
            return proveedorRepository.findById(proveedorId)
                    .map(entity -> {
                        ProveedorDTO dto = new ProveedorDTO();
                        dto.setId(entity.getId());
                        if (entity.getIdUsuario() != null) {
                            try {
                                var user = userClient.getUserById(entity.getIdUsuario());
                                if (user != null) {
                                    dto.setNombre(user.getNombre());
                                    dto.setCorreoContacto(user.getEmail());
                                }
                            } catch (Exception ex) {
                                
                            }
                        }
                        if (entity.getTipoProveedorId() != null) {
                            dto.setTipoProveedorId(entity.getTipoProveedorId());
                        }
                        dto.setActivo(true);
                        return dto;
                    })
                    .orElseGet(() -> {
                        return catalogClient.consultarProveedor(proveedorId);
                    });
        }
    }

    public UsuarioDTO consultarUsuarioRemoto(Integer usuarioId){
        return userClient.getUserById(usuarioId);
    }
}
