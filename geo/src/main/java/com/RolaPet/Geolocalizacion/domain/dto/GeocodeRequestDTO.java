package com.RolaPet.Geolocalizacion.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeocodeRequestDTO {

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private String ciudad;

    private String pais = "Colombia";
}
