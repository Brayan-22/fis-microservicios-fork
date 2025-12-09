package com.Rolapet.Noticia.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "foro")
@Data
public class Foro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String nombre;
}