package uni.fis.catalogo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                // Consultar catálogo por ID
                .requestMatchers("GET", "/api/catalogo/{id}")
                    .permitAll()
                
                // Listar productos de un catálogo
                .requestMatchers("GET", "/api/catalogo/{catalogoId}/listaProductos")
                    .permitAll()
                
                // Listar servicios de un catálogo
                .requestMatchers("GET", "/api/catalogo/{catalogoId}/listaServicios")
                    .permitAll()
                
                // Obtener producto específico
                .requestMatchers("GET", "/api/catalogo/{idCatalogo}/producto/{id}")
                    .permitAll()
                
                // Obtener servicio específico
                .requestMatchers("GET", "/api/catalogo/{idCatalogo}/servicio/{id}")
                    .permitAll()
                
                // Calificar producto
                .requestMatchers("POST", "/api/catalogo/producto/{id}/calificar")
                    .permitAll()
                
                // Calificar servicio
                .requestMatchers("POST", "/api/catalogo/servicio/{id}/calificar")
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
                
                // Cualquier otra petición requiere autenticación
                .anyRequest()
                    .authenticated()
            )
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(catalogoOwnershipFilter, JwtAuthenticationFilter.class);

        return http.build();
    }
}