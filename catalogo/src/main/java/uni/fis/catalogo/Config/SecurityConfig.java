package uni.fis.catalogo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import uni.fis.catalogo.Security.CatalogoOwnershipFilter;
import uni.fis.catalogo.Security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CatalogoOwnershipFilter catalogoOwnershipFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                // Permitir a todo el mundo ver endpoints GET del catálogo
                .requestMatchers(HttpMethod.GET, "/api/catalogo/**")
                    .permitAll()
                // Permitir accesso libre a los endpoints de actuator (health, info, etc.)
                .requestMatchers("/actuator/**")
                    .permitAll()
                
                // Crear catálogo
                .requestMatchers("POST", "/api/catalogo/crear")
                    .hasRole("PROVEEDOR")
                
                // Eliminar catálogo
                .requestMatchers("DELETE", "/api/catalogo/{id}/eliminar")
                    .hasRole("PROVEEDOR")
                
                // Agregar producto al catálogo
                .requestMatchers("POST", "/api/catalogo/{catalogoId}/producto")
                    .hasRole("PROVEEDOR")
                
                // Agregar servicio al catálogo
                .requestMatchers("POST", "/api/catalogo/{catalogoId}/servicio")
                    .hasRole("PROVEEDOR")
                
                // Eliminar producto
                .requestMatchers("DELETE", "/api/catalogo/{idCatalogo}/producto/{id}/eliminar")
                    .hasRole("PROVEEDOR")
                
                // Eliminar servicio
                .requestMatchers("DELETE", "/api/catalogo/{idCatalogo}/servicio/{id}/eliminar")
                    .hasRole("PROVEEDOR")
                
                // Requerir autenticación por defecto para cualquier otro request
                .anyRequest()
                    .authenticated()
            )
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(catalogoOwnershipFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}