package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeracionResponseDTO {
    private boolean aprobado;
    private String mensaje;
    private int numeroInfracciones;
}

