package com.RolaPet.Geolocalizacion.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReverseGeocodeRequestDTO {

    @NotNull(message = "La latitud es obligatoria")
    @Min(value = -90, message = "Latitud mínima: -90")
    @Max(value = 90, message = "Latitud máxima: 90")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    @Min(value = -180, message = "Longitud mínima: -180")
    @Max(value = 180, message = "Longitud máxima: 180")
    private Double longitud;
}
