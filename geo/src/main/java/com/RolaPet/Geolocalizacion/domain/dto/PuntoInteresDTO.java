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
public class PuntoInteresDTO {

    private Integer id;

    private String nombre;

    private String descripcion;

    private String imgPun;

    private Integer idDireccion;

    private DireccionDTO direccion;

    private String direccionCompleta;
}