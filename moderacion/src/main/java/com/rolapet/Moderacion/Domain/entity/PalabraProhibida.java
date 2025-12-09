package com.rolapet.Moderacion.Domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PalabraProhibida {
    private Integer id;
    private String palabra;
    private String descripcion;
    private Boolean activa;
}


