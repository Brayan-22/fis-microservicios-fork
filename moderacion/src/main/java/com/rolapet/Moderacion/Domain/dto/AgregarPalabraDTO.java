package com.rolapet.Moderacion.Domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para agregar nuevas palabras prohibidas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarPalabraDTO {

    @NotNull(message = "La palabra no puede ser nula")
    @NotBlank(message = "La palabra no puede estar vacía")
    @Size(min = 1, max = 200, message = "La palabra debe tener entre 1 y 200 caracteres")
    private String palabra;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;
}
