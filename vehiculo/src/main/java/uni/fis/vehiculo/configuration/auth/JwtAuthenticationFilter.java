package uni.fis.vehiculo.configuration.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("Entramos al if por lo que si tiene bearer");
            if (jwtUtil.validateToken(token)) {
                log.info("se pudo validar :D");
                var claims = jwtUtil.extractClaims(token);
                String username = claims.getSubject();

                // Crear autenticación
                var authorities = getAuthoritiesFromClaims(claims);

                var authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );
                log.info("La autenticación tenemos: {} ,{}", username, authorities);
                // Establecer en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private List<SimpleGrantedAuthority> getAuthoritiesFromClaims(io.jsonwebtoken.Claims claims) {
        return Collections.singletonList(new SimpleGrantedAuthority(claims.getSubject()));
    }
}