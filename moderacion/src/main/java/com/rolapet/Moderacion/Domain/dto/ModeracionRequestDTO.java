package com.rolapet.Moderacion.Domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeracionRequestDTO {
    @NotNull(message = "El contenido no puede ser nulo")
    @NotBlank(message = "El contenido no puede estar vacío")
    @Size(min = 1, max = 10000, message = "El contenido debe tener entre 1 y 10000 caracteres")
    private String contenido;
    @NotNull(message = "El ID de usuario no puede ser nulo")
    private Long usuarioId;
}


