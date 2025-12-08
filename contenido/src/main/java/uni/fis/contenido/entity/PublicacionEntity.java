package uni.fis.contenido.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
@Table(name = "publicacion")
public class PublicacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;

    @Column(name = "id_img")
    private Integer multimedia;

    @Column(name = "foro_id")
    private Integer foro;

}