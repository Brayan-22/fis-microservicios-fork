package com.Rolapet.Noticia.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(name = "apellido_1")
    private String apellido1;

    @Column(name = "apellido_2")
    private String apellido2;

    @Column(name = "fecha_de_nacimiento")
    private LocalDate fechaDeNacimiento;

    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "integer default 0")
    private Integer strikes;

    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "img_perfil")
    private Integer imgPerfil;
}
