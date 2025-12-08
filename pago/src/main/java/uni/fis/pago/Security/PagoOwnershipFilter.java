package uni.fis.pago.Security;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

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
import uni.fis.pago.Entity.Pago;
import uni.fis.pago.Repository.PagoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoOwnershipFilter extends OncePerRequestFilter {

    private final PagoRepository pagoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Permitir actuator sin validaciones
        if (path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Permitir crear pago sin autenticación
        if (method.equalsIgnoreCase("POST") && path.endsWith("/api/pago/crearPago")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Solo aplicar este filtro a rutas de /api/pago
        if (!path.contains("/api/pago")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            log.warn("Intento de acceso a /api/pago sin autenticación válida");
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado");
            return;
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer userId = userPrincipal.getUserId();

        log.debug("Usuario autenticado: userId={}", userId);

        try {
            Integer pagoId = extractPagoIdFromPath(path);

            if (pagoId == null) {
                log.warn("No se pudo extraer pagoId de la ruta: {}", path);
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, 
                    "No se pudo identificar el pago en la URL");
                return;
            }

            log.debug("PagoId extraído: {}", pagoId);

            if (!isOwnerOfPago(pagoId, userId, response)) {
                return;
            }

            log.debug("Validación de propiedad exitosa para userId={} en pagoId={}", userId, pagoId);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("Error en filtro de propiedad del pago", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error al validar permisos");
        }
    }

    private Integer extractPagoIdFromPath(String path) {
        try {
            if (path.contains("/ObtenerPago/")) {
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length - 1; i++) {
                    if ("ObtenerPago".equals(parts[i]) && parts[i + 1].matches("\\d+")) {
                        return Integer.parseInt(parts[i + 1]);
                    }
                }
            }

            if (path.contains("/agregarProducto")) {
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length; i++) {
                    if ("pago".equals(parts[i]) && i + 1 < parts.length 
                        && parts[i + 1].matches("\\d+")) {
                        return Integer.parseInt(parts[i + 1]);
                    }
                }
            }

            if (path.contains("/TerminarPago/")) {
                String[] parts = path.split("/");
                for (int i = 0; i < parts.length - 1; i++) {
                    if ("TerminarPago".equals(parts[i]) && parts[i + 1].matches("\\d+")) {
                        return Integer.parseInt(parts[i + 1]);
                    }
                }
            }

            if (path.contains("/producto/")) {
                return null;
            }

        } catch (Exception e) {
            log.error("Error extrayendo pagoId de: {}", path, e);
        }

        return null;
    }

    private boolean isOwnerOfPago(Integer pagoId, Integer userId, HttpServletResponse response) 
            throws IOException {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        
        if (pagoOpt.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Pago no encontrado");
            return false;
        }

        Integer idUsuario = pagoOpt.get().getIdUsuario();
        
        if (idUsuario == null || !Objects.equals(idUsuario, userId)) {
            log.warn("Usuario {} intentó acceder a pago {} que pertenece a {}", 
                userId, pagoId, idUsuario);
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, 
                "No autorizado - no eres propietario del pago");
            return false;
        }

        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) 
            throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }
    }
}