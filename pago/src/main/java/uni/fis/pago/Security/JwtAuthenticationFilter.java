package uni.fis.pago.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);

            if (!jwtService.isValid(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
                return;
            }

            String username = jwtService.getSubject(token);
            String role = jwtService.getRole(token);
            Integer userId = jwtService.getUserId(token);
            
            log.info("Usuario autenticado: userId={}, username={}, role={}", userId, username, role);
            
            var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            
            UserPrincipal userPrincipal = new UserPrincipal(userId, username, role);
            
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