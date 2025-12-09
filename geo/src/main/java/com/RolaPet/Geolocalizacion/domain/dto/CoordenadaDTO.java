package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoordenadaDTO {
    private Double latitud;
    private Double longitud;
    private String displayName;
    private String tipo;
}
