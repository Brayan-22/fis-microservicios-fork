package com.Rolapet.Noticia.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticiaDTO {
    private Integer id;
    private String titulo;
    private String texto;
    private LocalDateTime fecha;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String imagenUrl;
    private String imagenTipo;
    private AutorDTO autor;
    private Integer totalLikes;
    private Integer totalComentarios;
    private List<CategoriaDTO> categorias;
}
