package com.RolaPet.Geolocalizacion.domain.entity;

import com.RolaPet.Geolocalizacion.domain.enums.TipoUbicacion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "direccion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  //

    @Column(name = "via_principal", length = 255)
    private String viaPrincipal;

    @Column(name = "numero_via", length = 50)
    private String numeroVia;

    @Column(name = "letra_uno", length = 10)
    private String letraUno;

    @Column(name = "bis")
    private Boolean bis;

    @Column(name = "cardinalidad_uno", length = 10)
    private String cardinalidadUno;

    @Column(name = "numero_uno", length = 50)
    private String numeroUno;

    @Column(name = "letra_dos", length = 10)
    private String letraDos;

    @Column(name = "cardinalidad_dos", length = 10)
    private String cardinalidadDos;

    @Column(name = "numero_dos", length = 50)
    private String numeroDos;

    @Column(length = 255)
    private String complemento;

    // Método helper para construir la dirección completa como String
    public String getDireccionCompleta() {
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