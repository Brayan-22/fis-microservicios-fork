package uni.fis.contenido.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // WebSocket nativo (moderno)
        registry.addEndpoint("/ws-contenido")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*")  // Para compatibilidad en navegadores
                .withSockJS();

        // WebSocket estándar sin SockJS
        registry.addEndpoint("/ws-contenido")
                .setAllowedOriginPatterns("*")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");        // Servidor → Cliente
        registry.setApplicationDestinationPrefixes("/app");  // Cliente → Servidor
    }
}