package FIS.proyectoFinal.ProveedorService.Dominio.entidad;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="proveedor")
public class ProveedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "idUsuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_tipoproveedor", nullable = false)
    private Integer tipoProveedorId;
}
