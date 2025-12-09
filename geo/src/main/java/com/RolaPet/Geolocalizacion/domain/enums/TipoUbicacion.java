package com.RolaPet.Geolocalizacion.domain.enums;

/**
 * Enum para tipos de ubicación en el sistema WebSocket (solo memoria)
 * Este enum SÍ se usa en el WebSocket para diferenciar ubicaciones en memoria
 */
public enum TipoUbicacion {

    /**
     * Ubicación actual del usuario en tiempo real
     */
    ACTUAL("Ubicación Actual"),

    /**
     * Ubicación histórica (solo si creas tabla)
     */
    HISTORICO("Ubicación Histórica"),

    /**
     * Ubicación compartida con otros usuarios
     */
    COMPARTIDA("Ubicación Compartida");

    private final String descripcion;

    TipoUbicacion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}