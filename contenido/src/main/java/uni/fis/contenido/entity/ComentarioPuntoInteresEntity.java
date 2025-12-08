package uni.fis.contenido.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;



@Entity
@Data
@Table(name = "puntointerescomentario")
public class ComentarioPuntoInteresEntity {
    @Id 
    @Column(name = "puntocom_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "comentario_id")
    private ComentarioEntity comentario;

    @Column(name = "punto_id")
    private Integer idPunto;
}