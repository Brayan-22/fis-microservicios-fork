package com.Rolapet.Noticia.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "publicacion")
@Data
public class Publicacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String titulo;

    private LocalDateTime fecha;

    @Column(name = "id_img")
    private Integer idImg;

    @Column(name = "contenido_id", nullable = false)
    private Integer contenidoId;

    @Column(name = "foro_id")
    private Integer foroId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contenido_id", insertable = false, updatable = false)
    private Contenido contenido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_img", insertable = false, updatable = false)
    private Multimedia multimedia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foro_id", insertable = false, updatable = false)
    private Foro foro;
}