package com.Rolapet.Noticia.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ActualizarNoticiaRequest {
    @Size(max = 255, message = "El título no puede exceder 255 caracteres")
    private String titulo;

    private String texto;

    private String estado;

    private LocalDateTime fechaProgramada;

    private Integer idImg;

    private Integer foroId;

    private List<Integer> categorias;
}
