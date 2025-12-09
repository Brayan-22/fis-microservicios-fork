package com.RolaPet.Geolocalizacion.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Extraer usuario ID del header
            String usuarioIdStr = accessor.getFirstNativeHeader("X-Usuario-Id");

            if (usuarioIdStr != null && !usuarioIdStr.isEmpty()) {
                try {
                    Long usuarioId = Long.parseLong(usuarioIdStr);

                    // Guardar en la sesión WebSocket
                    accessor.getSessionAttributes().put("usuarioId", usuarioId);

                    log.info("Usuario {} autenticado en WebSocket - Session: {}",
                            usuarioId, accessor.getSessionId());

                } catch (NumberFormatException e) {
                    log.error("Usuario ID inválido en WebSocket: {}", usuarioIdStr);
                }
            } else {
                log.warn("Conexión WebSocket sin usuario ID - Session: {}",
                        accessor.getSessionId());
            }
        }

        // Registrar desconexiones
        if (accessor != null && StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            Long usuarioId = (Long) accessor.getSessionAttributes().get("usuarioId");
            log.info("Usuario {} desconectado de WebSocket - Session: {}",
                    usuarioId, accessor.getSessionId());
        }

        return message;
    }
}
