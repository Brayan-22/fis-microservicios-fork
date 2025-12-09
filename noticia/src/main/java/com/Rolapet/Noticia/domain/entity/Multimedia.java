package com.Rolapet.Noticia.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "multimedia")
@Data
public class Multimedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multi")
    private Integer id;

    @Column(length = 500)
    private String url;

    @Column(name = "tipo_archivo", length = 100)
    private String tipoArchivo;
}