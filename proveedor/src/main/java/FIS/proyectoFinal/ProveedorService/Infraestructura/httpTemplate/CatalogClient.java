package FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.DarBajoServicioRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarCatalogoRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarProductoRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS.PublicarServicioRequestDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ProductoDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ProveedorDTO;
import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.ServicioDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CatalogClient {

    private final RestTemplate restTemplate;
    private final String catalogBaseUrl;

    public CatalogClient(RestTemplate restTemplate,
                         @Value("${catalog.service.base-url:http://catalogo:8081}") String catalogBaseUrl) {
        this.restTemplate = restTemplate;
        this.catalogBaseUrl = catalogBaseUrl;
    }

    public ServicioDTO publicarServicio(PublicarServicioRequestDTO req) {
        Integer catalogoId = req.getCatalogoId();
        if (catalogoId == null) {
            throw new IllegalArgumentException("CatalogoId is required to publicarServicio");
        }
        String url = String.format("%s/api/catalogo/%d/servicio", catalogBaseUrl, catalogoId);
        ServicioRequestDTO catalogServicioRequest = mapServicioRequest(req);
        
        HttpHeaders headers = createAuthHeaders();
        log.debug("POST to {} with Authorization={}", url, maskAuth(headers.getFirst(HttpHeaders.AUTHORIZATION)));
        HttpEntity<ServicioRequestDTO> requestEntity = new HttpEntity<>(catalogServicioRequest, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, ServicioDTO.class).getBody();
    }

    public Long publicarCatalogo(PublicarCatalogoRequestDTO req) {
        HttpHeaders headers = createAuthHeaders();
        String url = catalogBaseUrl + "/api/catalogo/crear";
        log.debug("POST to {} with Authorization={}", url, maskAuth(headers.getFirst(HttpHeaders.AUTHORIZATION)));
        HttpEntity<PublicarCatalogoRequestDTO> requestEntity = new HttpEntity<>(req, headers);

        return restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            Long.class
        ).getBody();
    }
    public ProductoDTO publicarProducto(PublicarProductoRequestDTO req) {
        Integer catalogoId = req.getCatalogoId();
        if (catalogoId == null) {
            throw new IllegalArgumentException("CatalogoId is required to publicarProducto");
        }
        String url = String.format("%s/api/catalogo/%d/producto", catalogBaseUrl, catalogoId);
        ProductoRequestDTO catalogProductoRequest = mapProductoRequest(req);
        
        HttpHeaders headers = createAuthHeaders();
        log.debug("POST to {} with Authorization={}", url, maskAuth(headers.getFirst(HttpHeaders.AUTHORIZATION)));
        HttpEntity<ProductoRequestDTO> requestEntity = new HttpEntity<>(catalogProductoRequest, headers);

        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, ProductoDTO.class).getBody();
    }

    public ServicioDTO darDeBajaServicio(DarBajoServicioRequestDTO req) {
        try {
            if (req.getCatalogoId() == null) {
                throw new IllegalArgumentException("CatalogoId is required to darDeBajaServicio");
            }
            Integer catalogoId = req.getCatalogoId();
            String url = String.format("%s/api/catalogo/%d/servicio/%d/eliminar", catalogBaseUrl, catalogoId, req.getServicioId());
            
            HttpHeaders headers = createAuthHeaders();
            log.debug("DELETE to {} with Authorization={}", url, maskAuth(headers.getFirst(HttpHeaders.AUTHORIZATION)));
            HttpEntity<?> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<ServicioDTO> resp = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, ServicioDTO.class);
            return resp.getBody();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete service in catalog", ex);
        }
    }

    public ProveedorDTO consultarProveedor(Integer proveedorId) {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            catalogBaseUrl + "/api/catalogo/proveedor/" + proveedorId, 
            HttpMethod.GET, 
            requestEntity, 
            ProveedorDTO.class
        ).getBody();
    }
    
    private HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof FIS.proyectoFinal.ProveedorService.Infraestructura.security.UserPrincipal) {
                FIS.proyectoFinal.ProveedorService.Infraestructura.security.UserPrincipal p =
                    (FIS.proyectoFinal.ProveedorService.Infraestructura.security.UserPrincipal) auth.getPrincipal();
                if (p.getToken() != null && !p.getToken().isBlank()) {
                    String bearer = "Bearer " + p.getToken();
                    headers.set(HttpHeaders.AUTHORIZATION, bearer);
                    System.out.println("DEBUG: Header de autorización obtenido desde SecurityContext");
                    return headers;
                }
            }
        } catch (Exception e) {
        }
        String authHeader = getAuthorizationHeaderFromRequest();

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
            System.out.println("DEBUG: Header de autorización encontrado (fallback): " + authHeader.substring(0, Math.min(30, authHeader.length())) + "...");
        } else {
            System.out.println("DEBUG: No se encontró header de autorización válido (fallback): " + authHeader);
        }
        
        return headers;
    }
    
    private String getAuthorizationHeaderFromRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) 
                RequestContextHolder.currentRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            
            return request.getHeader(HttpHeaders.AUTHORIZATION);
        } catch (IllegalStateException e) {
            System.out.println("DEBUG: No se pudo obtener el request del contexto: " + e.getMessage());
            return null;
        }
    }

    private ServicioRequestDTO mapServicioRequest(PublicarServicioRequestDTO req) {
        String duracionString = null;
        return new ServicioRequestDTO(
            req.getNombre(),
            req.getPrecio(),
            req.getDuracion(),
            req.getHorario()
        );
    }

    private ProductoRequestDTO mapProductoRequest(PublicarProductoRequestDTO req) {
        return new ProductoRequestDTO(
            req.getNombre(),
            req.getPrecio(),
            req.getCantidad(),
            req.getTamano(),
            req.getPeso().toString(),
            req.getIdColor(),
            req.getIdUnidadPeso()
        );
    }

    private String maskAuth(String auth) {
        if (auth == null) return "<null>";
        if (auth.length() <= 20) return auth;
        return auth.substring(0, 10) + "..." + auth.substring(auth.length()-7);
    }
}