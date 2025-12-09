package com.RolaPet.Geolocalizacion.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthInterceptor authInterceptor;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar broker simple en memoria para /topic y /queue
        config.enableSimpleBroker("/topic", "/queue");

        // Prefijo para mensajes del cliente hacia el servidor
        config.setApplicationDestinationPrefixes("/app");

        // Prefijo para mensajes dirigidos a usuarios específicos
        config.setUserDestinationPrefix("/user");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-geolocalizacion")
                .setAllowedOriginPatterns("*") // En producción: especificar dominios exactos
                .withSockJS(); // Fallback para navegadores antiguos
    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}