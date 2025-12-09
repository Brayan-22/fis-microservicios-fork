package uni.fis.usuario.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import uni.fis.usuario.config.auth.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final @Qualifier(value = "customCorsConfigurationSource") CorsConfigurationSource corsConfig;

        // FilterChain para endpoints Basic Auth - ORDEN 1
        @Bean
        @Order(1)
        public SecurityFilterChain basicAuthSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/v1/validate/**", "/api/v1/users/**")
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfig))
                                .authorizeHttpRequests(authorize -> authorize
                                                .anyRequest().authenticated())
                                .httpBasic(Customizer.withDefaults())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        // FilterChain para endpoints JWT - ORDEN 2
        @Bean
        @Order(2)
        public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .securityMatcher("/api/v1/jwt/**") // ← Simplificado
                                .csrf(AbstractHttpConfigurer::disable)
                                .cors(cors -> cors.configurationSource(corsConfig))
                                .authorizeHttpRequests(authorize -> authorize
                                                .anyRequest().authenticated() // ← Cambiado a authenticated
                                )
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // ←
                                                                                                                      // AÑADIR
                                                                                                                      // JWT
                                                                                                                      // FILTER
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .formLogin(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        // FilterChain para Actuator - ORDEN 3
        @Bean
        @Order(3)
        public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .securityMatcher("/actuator/**") // ← Solo actuator
                                .authorizeHttpRequests(auth -> auth
                                                .anyRequest().permitAll())
                                .cors(cors -> cors.configurationSource(corsConfig))
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .build();
        }

        // ELIMINAR: publicSecurityFilterChain y securityWebFilterChain
        // porque causan conflicto con "/**"

        @Bean
        public UserDetailsService userDetailsService() {
                UserDetails user = User.withUsername("admin")
                                .password("{noop}admin123")
                                .roles("USER")
                                .build();
                return new InMemoryUserDetailsManager(user);
        }
}