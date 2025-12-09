package FIS.proyectoFinal.ProveedorService.Infraestructura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import FIS.proyectoFinal.ProveedorService.Infraestructura.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**")
                    .permitAll()
                .requestMatchers("GET", "/api/proveedores/usuario/{usuarioId}")
                    .permitAll()
                .requestMatchers("POST", "/api/proveedores/servicios")
                    .hasRole("PROVEEDOR")
                .requestMatchers("POST", "/api/proveedores/catalogos")
                    .hasRole("PROVEEDOR")
                .requestMatchers("POST", "/api/proveedores/productos")
                    .hasRole("PROVEEDOR")
                .requestMatchers("POST", "/api/proveedores/comentarios")
                    .hasRole("PROVEEDOR")
                .requestMatchers("DELETE", "/api/proveedores/servicios/{servicioId}")
                    .hasRole("PROVEEDOR")
                .anyRequest()
                    .authenticated()
            )
            
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}