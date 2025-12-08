package uni.fis.pago.Security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uni.fis.pago.Entity.OrdenCompra;
import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Entity.Pago;
import uni.fis.pago.Repository.OrdenCompraRepository;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Repository.PagoRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class PagoOwnershipFilter extends OncePerRequestFilter {

    private final PagoRepository pagoRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenItemRepository ordenItemRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest requestToUse = (request instanceof ContentCachingRequestWrapper)
                ? request
                : new ContentCachingRequestWrapper(request);

        String path = requestToUse.getRequestURI();
        String method = requestToUse.getMethod();

        log.debug("PagoOwnershipFilter - Path: {}, Method: {}", path, method);

        if (method.equalsIgnoreCase("POST") && path.endsWith("/api/pago/crearPago")) {
            log.debug("CrearPago endpoint - permitiendo sin verificación de propietario");
            filterChain.doFilter(requestToUse, response);
            return;
        }

        if (!path.contains("/api/pago")) {
            filterChain.doFilter(requestToUse, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado");
            return;
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer userId = userPrincipal.getUserId();

        try {
            if (method.equalsIgnoreCase("GET") && path.matches(".*/api/pago/ObtenerPago/\\d+.*")) {
                Integer pagoId = extractIdFromPath(path, "/ObtenerPago/");
                if (pagoId == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID de pago inválido");
                    return;
                }
                if (!isOwnerOfPago(pagoId, userId, response)) return;
                filterChain.doFilter(requestToUse, response);
                return;
            }

            if ((method.equalsIgnoreCase("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+.*"))
                    || (method.equalsIgnoreCase("DELETE") && path.matches(".*/api/pago/EliminarOrdenCompra/\\d+.*"))
                    || (method.equalsIgnoreCase("GET") && path.matches(".*/api/pago/ObtenerOrdenCompra/\\d+/OrdenesItems.*"))) {

                Integer ordenCompraId = extractIdFromPath(path,
                        path.contains("/EliminarOrdenCompra/") ? "/EliminarOrdenCompra/" : "/ObtenerOrdenCompra/");
                if (ordenCompraId == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID de orden inválido");
                    return;
                }
                Optional<OrdenCompra> ordenOpt = ordenCompraRepository.findById(ordenCompraId);
                if (ordenOpt.isEmpty()) {
                    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Orden no encontrada");
                    return;
                }
                Integer idPago = ordenOpt.get().getIdPago();
                if (idPago == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Orden sin pago asociado");
                    return;
                }
                if (!isOwnerOfPago(idPago, userId, response)) return;
                filterChain.doFilter(requestToUse, response);
                return;
            }

            if ((method.equalsIgnoreCase("GET") && path.matches(".*/api/pago/ObtenerOrdenItem/\\d+.*"))
                    || (method.equalsIgnoreCase("DELETE") && path.matches(".*/api/pago/EliminarOrdenItem/\\d+.*"))) {

                // extraer el itemId correctamente: si la URL tiene varios números, tomar el último (itemId)
                Integer itemId = extractItemIdFromPath(path,
                        path.contains("/EliminarOrdenItem/") ? "/EliminarOrdenItem/" : "/ObtenerOrdenItem/");
                if (itemId == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "ID de item inválido o marcador no encontrado en la ruta");
                    return;
                }

                // Asegurarnos de buscar el item en el repositorio de items (NO en ordenCompra)
                Optional<OrdenItem> itemOpt = ordenItemRepository.findById(itemId);
                if (itemOpt.isEmpty()) {
                    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Item no encontrado");
                    return;
                }

                // Buscar la orden que referencia a este item usando el repositorio
                // (la relación está en OrdenCompra.idOrdenItem, no en OrdenItem)
                OrdenCompra orden = ordenCompraRepository.findByIdOrdenItem(itemId);
                if (orden == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Orden asociada no encontrada");
                    return;
                }

                Integer idPago = orden.getIdPago();
                if (!isOwnerOfPago(idPago, userId, response)) return;
                filterChain.doFilter(requestToUse, response);
                return;
            }

            // 4) POST crearOrdenCompra o crearOrdenItem: intentar extraer idPago del body o parámetros y verificar propietario
            if (method.equalsIgnoreCase("POST") && (path.endsWith("/api/pago/crearOrdenCompra") || path.endsWith("/api/pago/crearOrdenItem"))) {
                Integer idPago = null;
                // try query param first
                String idPagoParam = requestToUse.getParameter("idPago");
                if (idPagoParam == null) idPagoParam = requestToUse.getParameter("id_pago");
                if (idPagoParam != null && idPagoParam.matches("\\d+")) {
                    idPago = Integer.parseInt(idPagoParam);
                } else {
                    // try to read from body (JSON) - wrapper used above
                    try {
                        ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) requestToUse;
                        byte[] bodyBytes = wrapper.getInputStream().readAllBytes();
                        String body = new String(bodyBytes, StandardCharsets.UTF_8);
                        idPago = extractIdFromBody(body, "idPago");
                        if (idPago == null) idPago = extractIdFromBody(body, "id_pago");
                    } catch (Exception e) {
                        log.warn("No se pudo leer body para extraer idPago: {}", e.getMessage());
                    }
                }

                if (idPago == null) {
                    sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "No se pudo determinar idPago para verificar propietario");
                    return;
                }
                if (!isOwnerOfPago(idPago, userId, response)) return;
                filterChain.doFilter(requestToUse, response);
                return;
            }

            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Acceso no permitido");
            return;

        } catch (Exception e) {
            log.error("Error en filtro ownership", e);
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno de seguridad");
            return;
        }
    }

    private boolean isOwnerOfPago(Integer pagoId, Integer userId, HttpServletResponse response) throws IOException {
        Optional<Pago> pagoOpt = pagoRepository.findById(pagoId);
        if (pagoOpt.isEmpty()) {
            sendErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, "Pago no encontrado");
            return false;
        }
        Integer idUsuario = pagoOpt.get().getIdUsuario();
        if (idUsuario == null || !Objects.equals(idUsuario, userId)) {
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "No autorizado - no eres propietario del pago");
            return false;
        }
        return true;
    }

    private Integer extractIdFromPath(String path, String marker) {
        try {
            if (marker == null || marker.isEmpty()) return null;
            int startIndex = path.indexOf(marker);
            if (startIndex == -1) {
                log.debug("Marcador '{}' no encontrado en la ruta '{}'", marker, path);
                return null;
            }
            String afterMarker = path.substring(startIndex + marker.length());
            // extraer solo los dígitos iniciales después del marcador
            Matcher m = Pattern.compile("^(\\d+)").matcher(afterMarker);
            if (m.find()) {
                String idStr = m.group(1);
                return Integer.parseInt(idStr);
            } else {
                log.debug("No se encontró un id numérico inmediatamente después del marcador '{}' en '{}'", marker, path);
                return null;
            }
        } catch (Exception e) {
            log.error("Error extrayendo id desde la ruta '{}', marcador '{}': {}", path, marker, e.getMessage());
            return null;
        }
    }

    private Integer extractItemIdFromPath(String path, String marker) {
        try {
            if (marker == null || marker.isEmpty()) return null;
            int startIndex = path.indexOf(marker);
            if (startIndex == -1) {
                log.debug("Marcador '{}' no encontrado en la ruta '{}'", marker, path);
                return null;
            }
            String afterMarker = path.substring(startIndex + marker.length());
            Matcher m = Pattern.compile("(\\d+)").matcher(afterMarker);
            Integer lastId = null;
            while (m.find()) {
                lastId = Integer.parseInt(m.group(1));
            }
            if (lastId != null) return lastId;

            // fallback: si no hay números tras el marcador, intentar buscar el último número en toda la ruta
            Matcher m2 = Pattern.compile("(\\d+)").matcher(path);
            Integer lastGlobal = null;
            while (m2.find()) {
                lastGlobal = Integer.parseInt(m2.group(1));
            }
            return lastGlobal;
        } catch (Exception e) {
            log.error("Error extrayendo itemId desde la ruta '{}', marcador '{}': {}", path, marker, e.getMessage());
            return null;
        }
    }

    private Integer extractIdFromBody(String body, String key) {
        if (body == null || body.isBlank()) return null;
        String patternStr = "\"?" + Pattern.quote(key) + "\"?\\s*[:=]\\s*\"?(\\d+)\"?";
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(body);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (NumberFormatException ignored) {}
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        if (!response.isCommitted()) {
            response.setStatus(status);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + message + "\"}");
        }
    }

}