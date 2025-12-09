package com.RolaPet.Geolocalizacion.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DireccionDTO {
    private Integer id;
    private String viaPrincipal;
    private String numeroVia;
    private String letraUno;
    private Boolean bis;
    private String cardinalidadUno;
    private String numeroUno;
    private String letraDos;
    private String cardinalidadDos;
    private String numeroDos;
    private String complemento;

    // Campo calculado para mostrar la dirección completa formateada
    private String direccionCompleta;

    // Método helper para construir la dirección completa
    public String buildDireccionCompleta() {
        StringBuilder direccion = new StringBuilder();

        if (viaPrincipal != null) direccion.append(viaPrincipal).append(" ");
        if (numeroVia != null) direccion.append(numeroVia);
        if (letraUno != null) direccion.append(letraUno);
        if (Boolean.TRUE.equals(bis)) direccion.append(" BIS");
        if (cardinalidadUno != null) direccion.append(" ").append(cardinalidadUno);
        if (numeroUno != null) direccion.append(" # ").append(numeroUno);
        if (letraDos != null) direccion.append(letraDos);
        if (cardinalidadDos != null) direccion.append(" ").append(cardinalidadDos);
        if (numeroDos != null) direccion.append(" - ").append(numeroDos);
        if (complemento != null) direccion.append(" ").append(complemento);

        return direccion.toString().trim();
    }
}
