package fis.auth.infrastructure.service.security.auth;

import fis.auth.domain.service.TokenStrategy;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenStrategy jwtTokenStrategy;
    private final JwtTokenInfoExtractor tokenInfoExtractor;

    public JwtAuthenticationFilter(@Qualifier("JWTTokenStrategy") TokenStrategy jwtTokenStrategy,
                                   JwtTokenInfoExtractor tokenInfoExtractor) {
        this.jwtTokenStrategy = jwtTokenStrategy;
        this.tokenInfoExtractor = tokenInfoExtractor;
    }

    //
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/v1/auth/login") ||
                path.equals("/api/v1/auth/refresh") ||
                path.equals("/api/v1/auth/sign-in");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String token = getTokenFromRequest(request);

        if (token != null) {
            jwtTokenStrategy.validate(token);

            String username = tokenInfoExtractor.getUsername(token);
            String role = tokenInfoExtractor.getRole(token);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + role))
                    );

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
