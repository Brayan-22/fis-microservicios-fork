package com.RolaPet.Geolocalizacion.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteresRequestDTO {

    /**
     * Nombre del punto de interés
     * Máximo 20 caracteres según el esquema de BD
     * Ejemplo: "Taller Central"
     */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre debe tener entre 3 y 20 caracteres")
    private String nombre;

    /**
     * Descripción breve
     * Máximo 50 caracteres según el esquema de BD
     * Ejemplo: "Servicio rápido y garantizado"
     */
    @Size(max = 50, message = "La descripción no puede exceder 50 caracteres")
    private String descripcion;

    /**
     * URL de la imagen del punto
     * Máximo 500 caracteres
     * Ejemplo: "https://ejemplo.com/imagenes/taller.jpg"
     */
    @Pattern(
            regexp = "^(https?://).*\\.(jpg|jpeg|png|gif|webp)$",
            message = "URL de imagen inválida. Debe ser JPG, PNG, GIF o WEBP"
    )
    @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    private String imgPun;

    /**
     * Dirección del punto de interés
     * Objeto completo con formato colombiano
     */
    @NotNull(message = "La dirección es obligatoria")
    private DireccionDTO direccion;
}

