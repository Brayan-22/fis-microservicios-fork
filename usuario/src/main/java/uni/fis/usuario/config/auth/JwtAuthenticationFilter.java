package uni.fis.usuario.config.auth;

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

import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info("Entramos al if por lo que si tiene bearer");
            if (jwtUtil.validateToken(token)) {
                log.info("se pudo validar :D");
                var claims = jwtUtil.extractClaims(token);
                // Crear autenticación
                var authorities = getAuthoritiesFromClaims(claims);

                var authentication = new UsernamePasswordAuthenticationToken(
                        claims,
                        null,
                        authorities);
                log.info("La autenticación tenemos: {} ,{}", claims, authorities);
                // Establecer en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Collection<SimpleGrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
        String role = claims.get("rol", String.class);

        if (role == null) {
            return Collections.emptyList();
        }

        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }


}