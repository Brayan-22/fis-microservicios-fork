package FIS.proyectoFinal.ProveedorService.Aplicacion.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import FIS.proyectoFinal.ProveedorService.Aplicacion.repository.ProveedorRepository;
import FIS.proyectoFinal.ProveedorService.Dominio.entidad.ProveedorEntity;
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
import FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate.CatalogClient;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;
    private final CatalogClient catalogClient;

    public ProveedorService(ProveedorRepository proveedorRepository, CatalogClient catalogClient){
        this.proveedorRepository = proveedorRepository;
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
        } 
        ProveedorEntity proveedor = proveedorRepository.findById(proveedorId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor not found"));
        ProveedorDTO dto = new ProveedorDTO();
        dto.setId(proveedor.getId());
        dto.setIdUsuario(proveedor.getIdUsuario());
        dto.setTipoProveedorId(proveedor.getTipoProveedorId());
        return dto;
    }
}
