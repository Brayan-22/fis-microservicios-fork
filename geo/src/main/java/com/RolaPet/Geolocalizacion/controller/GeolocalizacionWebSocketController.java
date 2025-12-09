package com.RolaPet.Geolocalizacion.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket Controller para ubicación en tiempo real
 * SIN BASE DE DATOS - Solo almacena en memoria durante la sesión
 *
 * LIMITACIONES:
 * - No hay historial (se pierde al cerrar sesión)
 * - No hay persistencia (se pierde al reiniciar servidor)
 * - Solo ubicación "actual" de usuarios conectados
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class GeolocalizacionWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    // Almacenamiento EN MEMORIA de ubicaciones actuales
    // Map<usuarioId, UbicacionEnMemoria>
    private final Map<Long, UbicacionEnMemoria> ubicacionesActuales = new ConcurrentHashMap<>();

    // ============================================
    // UBICACIÓN EN TIEMPO REAL (SOLO MEMORIA)
    // ============================================

    /**
     * Actualizar ubicación del usuario (se guarda SOLO en memoria)
     *
     * Cliente envía a: /app/ubicacion/actualizar
     * Respuesta: /user/queue/ubicacion/confirmacion
     * Broadcast: /topic/ubicaciones/actualizada
     */
    @MessageMapping("/ubicacion/actualizar")
    public void actualizarUbicacion(
            @Payload Map<String, Object> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        Long usuarioId = obtenerUsuarioId(headerAccessor);

        Double latitud = Double.valueOf(payload.get("latitud").toString());
        Double longitud = Double.valueOf(payload.get("longitud").toString());
        Double precision = payload.containsKey("precision")
                ? Double.valueOf(payload.get("precision").toString())
                : null;

        log.info("[WebSocket] Ubicación recibida - Usuario: {} → ({}, {})",
                usuarioId, latitud, longitud);

        try {
            // Validar coordenadas
            validarCoordenadas(latitud, longitud);

            // Guardar en memoria (se pierde al reiniciar)
            UbicacionEnMemoria ubicacion = new UbicacionEnMemoria(
                    usuarioId, latitud, longitud, precision, LocalDateTime.now()
            );
            ubicacionesActuales.put(usuarioId, ubicacion);

            log.info("Ubicación guardada EN MEMORIA (no persiste en BD)");

            // Confirmar al usuario
            messagingTemplate.convertAndSendToUser(
                    usuarioId.toString(),
                    "/queue/ubicacion/confirmacion",
                    Map.of(
                            "status", "success",
                            "mensaje", "Ubicación actualizada (solo en memoria)",
                            "ubicacion", Map.of(
                                    "usuarioId", usuarioId,
                                    "latitud", latitud,
                                    "longitud", longitud,
                                    "precision", precision,
                                    "timestamp", LocalDateTime.now()
                            ),
                            "timestamp", System.currentTimeMillis()
                    )
            );

            // Broadcast a todos (opcional)
            messagingTemplate.convertAndSend(
                    "/topic/ubicaciones/actualizada",
                    Map.of(
                            "usuarioId", usuarioId,
                            "latitud", latitud,
                            "longitud", longitud,
                            "timestamp", System.currentTimeMillis()
                    )
            );

            log.info("[WebSocket] Ubicación transmitida");

        } catch (Exception e) {
            log.error("[WebSocket] Error: {}", e.getMessage());
            enviarError(usuarioId, e.getMessage());
        }
    }

    /**
     * Consultar ubicación actual de un usuario (solo si está en memoria)
     */
    @MessageMapping("/ubicacion/consultar")
    public void consultarUbicacion(
            @Payload Map<String, Long> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        Long solicitanteId = obtenerUsuarioId(headerAccessor);
        Long usuarioConsultado = payload.get("usuarioId");

        log.info("🔍 [WebSocket] Usuario {} consultando ubicación de {}",
                solicitanteId, usuarioConsultado);

        try {
            UbicacionEnMemoria ubicacion = ubicacionesActuales.get(usuarioConsultado);

            if (ubicacion == null) {
                messagingTemplate.convertAndSendToUser(
                        solicitanteId.toString(),
                        "/queue/ubicacion/actual",
                        Map.of(
                                "status", "not_found",
                                "mensaje", "Usuario no tiene ubicación activa o no está conectado",
                                "usuarioId", usuarioConsultado
                        )
                );
                return;
            }

            messagingTemplate.convertAndSendToUser(
                    solicitanteId.toString(),
                    "/queue/ubicacion/actual",
                    Map.of(
                            "status", "success",
                            "usuarioId", usuarioConsultado,
                            "ubicacion", Map.of(
                                    "latitud", ubicacion.latitud,
                                    "longitud", ubicacion.longitud,
                                    "precision", ubicacion.precision,
                                    "timestamp", ubicacion.timestamp
                            )
                    )
            );

            log.info("Ubicación enviada");

        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            enviarError(solicitanteId, e.getMessage());
        }
    }

    /**
     * Listar usuarios conectados con ubicación activa
     */
    @MessageMapping("/ubicacion/conectados")
    public void listarConectados(SimpMessageHeaderAccessor headerAccessor) {

        Long usuarioId = obtenerUsuarioId(headerAccessor);

        log.info("[WebSocket] Usuario {} consultando usuarios conectados", usuarioId);

        List<Map<String, Object>> usuariosConectados = new ArrayList<>();

        ubicacionesActuales.forEach((userId, ubicacion) -> {
            usuariosConectados.add(Map.of(
                    "usuarioId", userId,
                    "latitud", ubicacion.latitud,
                    "longitud", ubicacion.longitud,
                    "timestamp", ubicacion.timestamp
            ));
        });

        messagingTemplate.convertAndSendToUser(
                usuarioId.toString(),
                "/queue/ubicacion/conectados",
                Map.of(
                        "status", "success",
                        "total", usuariosConectados.size(),
                        "usuarios", usuariosConectados
                )
        );

        log.info(" Enviados {} usuarios conectados", usuariosConectados.size());
    }

    /**
     * Buscar usuarios cercanos (en memoria)
     */
    @MessageMapping("/ubicacion/cercanos")
    public void buscarCercanos(
            @Payload Map<String, Double> payload,
            SimpMessageHeaderAccessor headerAccessor) {

        Long usuarioId = obtenerUsuarioId(headerAccessor);
        Double radioKm = payload.getOrDefault("radioKm", 5.0);

        log.info("Usuario {} buscando cercanos (radio: {}km)", usuarioId, radioKm);

        UbicacionEnMemoria miUbicacion = ubicacionesActuales.get(usuarioId);

        if (miUbicacion == null) {
            enviarError(usuarioId, "No tienes ubicación activa");
            return;
        }

        List<Map<String, Object>> cercanos = new ArrayList<>();

        ubicacionesActuales.forEach((otroUserId, otraUbicacion) -> {
            if (!otroUserId.equals(usuarioId)) {
                double distancia = calcularDistancia(
                        miUbicacion.latitud, miUbicacion.longitud,
                        otraUbicacion.latitud, otraUbicacion.longitud
                );

                if (distancia <= radioKm) {
                    cercanos.add(Map.of(
                            "usuarioId", otroUserId,
                            "latitud", otraUbicacion.latitud,
                            "longitud", otraUbicacion.longitud,
                            "distanciaKm", Math.round(distancia * 100.0) / 100.0
                    ));
                }
            }
        });

        // Ordenar por distancia
        cercanos.sort((a, b) ->
                Double.compare((Double) a.get("distanciaKm"), (Double) b.get("distanciaKm"))
        );

        messagingTemplate.convertAndSendToUser(
                usuarioId.toString(),
                "/queue/ubicacion/cercanos",
                Map.of(
                        "status", "success",
                        "total", cercanos.size(),
                        "radioKm", radioKm,
                        "usuarios", cercanos
                )
        );

        log.info("Encontrados {} usuarios cercanos", cercanos.size());
    }

    /**
     * Desconectar (eliminar ubicación de memoria)
     */
    @MessageMapping("/ubicacion/desconectar")
    public void desconectar(SimpMessageHeaderAccessor headerAccessor) {

        Long usuarioId = obtenerUsuarioId(headerAccessor);

        log.info("Usuario {} desconectando ubicación", usuarioId);

        ubicacionesActuales.remove(usuarioId);

        messagingTemplate.convertAndSend(
                "/topic/ubicaciones/desconectada",
                Map.of("usuarioId", usuarioId)
        );

        log.info("Ubicación eliminada de memoria");
    }

    // ============================================
    // MÉTODOS AUXILIARES
    // ============================================

    private Long obtenerUsuarioId(SimpMessageHeaderAccessor headerAccessor) {
        Long usuarioId = (Long) headerAccessor.getSessionAttributes().get("usuarioId");

        if (usuarioId == null) {
            usuarioId = 1L;
            log.warn("Usuario no autenticado, usando ID default");
        }

        return usuarioId;
    }

    private void enviarError(Long usuarioId, String mensaje) {
        messagingTemplate.convertAndSendToUser(
                usuarioId.toString(),
                "/queue/error",
                Map.of(
                        "status", "error",
                        "mensaje", mensaje,
                        "timestamp", System.currentTimeMillis()
                )
        );
    }

    private void validarCoordenadas(Double lat, Double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitud inválida");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitud inválida");
        }
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // ============================================
    // CLASE INTERNA PARA UBICACIÓN EN MEMORIA
    // ============================================

    private static class UbicacionEnMemoria {
        Long usuarioId;
        Double latitud;
        Double longitud;
        Double precision;
        LocalDateTime timestamp;

        UbicacionEnMemoria(Long usuarioId, Double latitud, Double longitud,
                           Double precision, LocalDateTime timestamp) {
            this.usuarioId = usuarioId;
            this.latitud = latitud;
            this.longitud = longitud;
            this.precision = precision;
            this.timestamp = timestamp;
        }
    }
}