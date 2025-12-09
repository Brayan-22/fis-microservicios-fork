package com.RolaPet.Geolocalizacion.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "punto_de_interes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PuntoInteres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String nombre;

    @Column(length = 50)
    private String descripcion;

    @Column(name = "img_pun", length = 500)
    private String imgPun;

    @Column(name = "id_direccion")
    private Integer idDireccion;

    // Relación con Direccion (opcional, pero recomendado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_direccion", insertable = false, updatable = false)
    private Direccion direccion;
}