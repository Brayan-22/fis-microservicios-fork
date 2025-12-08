package uni.fis.catalogo.Security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uni.fis.catalogo.Repository.CatalogoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogoOwnershipFilter extends OncePerRequestFilter {

    private final CatalogoRepository catalogoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        
        log.info("=== OWNERSHIP FILTER === Path: {}, Method: {}", path, method);
        
        boolean requiresOwnershipCheck = 
            (method.equals("POST") && path.matches(".*/api/catalogo/crear.*")) ||
            (method.equals("POST") && path.matches(".*/api/catalogo/\\d+/producto.*")) ||
            (method.equals("POST") && path.matches(".*/api/catalogo/\\d+/servicio.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/catalogo/\\d+/producto/\\d+/eliminar.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/catalogo/\\d+/servicio/\\d+/eliminar.*")) ||
            (method.equals("DELETE") && path.matches(".*/api/catalogo/\\d+/eliminar.*"));

        if (!requiresOwnershipCheck) {
            log.info("No requiere verificación de ownership, continuando...");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null) {
                log.error("Authentication es null");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Usuario no autenticado\"}");
                return;
            }
            
            log.info("Authentication principal type: {}", authentication.getPrincipal().getClass().getName());
            
            if (!(authentication.getPrincipal() instanceof UserPrincipal)) {
                log.error("Principal no es instancia de UserPrincipal");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Usuario no autenticado correctamente\"}");
                return;
            }

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            Integer userId = userPrincipal.getUserId();
            
            log.info("UserId del token: {}", userId);

            if (method.equals("POST") && path.matches(".*/api/catalogo/crear.*")) {
                log.info("Creación de catálogo, sin verificación de ownership previo");
                filterChain.doFilter(request, response);
                return;
            }

            Integer catalogoId = extractCatalogoId(path);
            
            if (catalogoId == null) {
                log.error("No se pudo extraer catalogoId de la ruta: {}", path);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"No se pudo identificar el catálogo en la URL\"}");
                return;
            }
            
            log.info("CatalogoId extraído: {}", catalogoId);

            var catalogoOpt = catalogoRepository.findById(catalogoId);
            
            if (catalogoOpt.isEmpty()) {
                log.warn("Catálogo con id {} no encontrado", catalogoId);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Catálogo no encontrado\"}");
                return;
            }

            var catalogo = catalogoOpt.get();
            Integer idProveedor = catalogo.getIdProveedor();
            
            log.info("IdProveedor del catálogo: {}", idProveedor);
            log.info("Comparando userId {} con idProveedor {}", userId, idProveedor);

            if (!idProveedor.equals(userId)) {
                log.warn("Usuario {} intentó acceder al catálogo {} que pertenece al proveedor {}", 
                         userId, catalogoId, idProveedor);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"No tienes permiso para modificar este catálogo\"}");
                return;
            }
            
            log.info("Verificación de ownership exitosa para userId {} en catalogoId {}", userId, catalogoId);

        } catch (NumberFormatException e) {
            log.error("Error al parsear número de la ruta: {}", path, e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Formato de URL inválido\"}");
            return;
        } catch (Exception e) {
            log.error("Error inesperado en la verificación de ownership", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Error al verificar permisos: " + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Integer extractCatalogoId(String path) {
        try {
            log.debug("Extrayendo catalogoId de: {}", path);
            
            if (path.contains("/api/catalogo/")) {
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("catalogo") && i + 1 < parts.length) {
                        String catalogoIdStr = parts[i + 1];
                        if (!catalogoIdStr.equals("crear") && catalogoIdStr.matches("\\d+")) {
                            log.debug("Encontrado catalogoId después de 'catalogo': {}", catalogoIdStr);
                            return Integer.parseInt(catalogoIdStr);
                        }
                    }
                }
            }
            
            String[] parts = path.split("/");
            if (parts.length > 1) {
                for (String part : parts) {
                    if (!part.isEmpty() && part.matches("\\d+")) {
                        log.debug("Encontrado primer número en la ruta: {}", part);
                        return Integer.parseInt(part);
                    }
                }
            }
        } catch (NumberFormatException e) {
            log.error("Error al parsear catalogoId de la ruta: {}", path, e);
        } catch (Exception e) {
            log.error("Error inesperado extrayendo catalogoId de la ruta: {}", path, e);
        }
        return null;
    }
}