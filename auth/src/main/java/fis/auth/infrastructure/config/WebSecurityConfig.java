package fis.auth.infrastructure.config;

import fis.auth.infrastructure.service.security.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

        private final @Qualifier(value = "customCorsConfigurationSource") CorsConfigurationSource corsConfig;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        // FilterChain principal con JWT
        @Bean
        @Order(1)
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .securityMatcher("/api/**") // ← Solo endpoints /api/**
                        .csrf(AbstractHttpConfigurer::disable)
                        .cors(cors -> cors.configurationSource(corsConfig))
                        .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(
                                        "/api/v1/auth/login",
                                        "/api/v1/auth/refresh",
                                        "/api/v1/auth/sign-in")
                                .permitAll()
                                .anyRequest().authenticated())
                        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

                return http.build();
        }

        // FilterChain para Actuator, Swagger, etc.
        @Bean
        @Order(2)
        public SecurityFilterChain publicEndpointsFilterChain(HttpSecurity http) throws Exception {
                return http
                        .securityMatcher("/actuator/**", "/v3/api-docs/**", "/swagger-ui/**",
                                "/swagger-ui.html") // ← Solo estos
                        .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll() // Todos públicos
                        )
                        .csrf(AbstractHttpConfigurer::disable)
                        .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .build();
        }
}