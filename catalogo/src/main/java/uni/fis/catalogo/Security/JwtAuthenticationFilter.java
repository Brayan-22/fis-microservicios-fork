package uni.fis.catalogo.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uni.fis.catalogo.Repository.ProveedorRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final ProveedorRepository proveedorRepository;

    public JwtAuthenticationFilter(JwtService jwtService, ProveedorRepository proveedorRepository) {
        this.jwtService = jwtService;
        this.proveedorRepository = proveedorRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Skip token processing for actuator endpoints
        String path = request.getRequestURI();
        if (path != null && path.startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        log.debug("Incoming Authorization header: {}", authHeader == null ? "<null>" : authHeader.substring(0, Math.min(50, authHeader.length())) + "...");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (!jwtService.isValid(token)) {
                log.warn("Token inválido o expirado al validar en catalogo");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
                return;
            }

            String username = jwtService.getSubject(token);
            String role = jwtService.getRole(token);
            Integer userId = jwtService.getUserId(token);
            log.debug("Token valid. subject={}, role={}, userId={}", username, role, userId);
            
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            Integer proveedorId = null;
            try {
                if (userId != null) {
                    proveedorId = proveedorRepository.findIdByIdUsuario(userId);
                }
            } catch (Exception ex) {
                log.warn("No se pudo consultar ProveedorRepository: {}", ex.getMessage());
            }

            UserPrincipal userPrincipal = new UserPrincipal(userId, username, role, proveedorId);

            var authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, authorities);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("Error al procesar el token", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Error al procesar el token: " + e.getMessage() + "\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}