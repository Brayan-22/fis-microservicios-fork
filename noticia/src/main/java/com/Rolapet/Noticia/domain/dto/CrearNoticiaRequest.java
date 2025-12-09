package com.Rolapet.Noticia.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CrearNoticiaRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String texto;

    private String estado; // "publicado" o "borrador"

    private LocalDateTime fechaProgramada;

    private Integer idImg;

    private Integer foroId;

    private List<Integer> categorias;
}