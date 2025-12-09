package uni.fis.usuario.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "documento")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tipodocu")
    @JsonBackReference
    private TipoDocumentoEntity tipoDocumento;

    @Column(name = "numero_string", length = 50)
    private String numeroDocumento;

    @Column(name = "fecha_expedicion", nullable = false)
    private LocalDateTime fechaExpedicion;
}