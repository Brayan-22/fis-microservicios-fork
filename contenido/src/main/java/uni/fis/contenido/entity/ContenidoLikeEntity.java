package uni.fis.contenido.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "likeContenido")
public class ContenidoLikeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @ManyToOne
    @JoinColumn(name = "contenido_id")
    private ContenidoEntity contenido;

}
