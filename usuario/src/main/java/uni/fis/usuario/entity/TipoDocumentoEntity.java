package uni.fis.usuario.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tipodocumento")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoDocumentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipodocu")
    private Integer id;
    @Column(name = "nombre", length = 100, unique = true) // debe ser unica
    private String nombre;

    // relacion
    @OneToMany(mappedBy = "tipoDocumento")
    @JsonManagedReference
    private Set<DocumentoEntity> listaDocumentos = new HashSet<>();


    public TipoDocumentoEntity(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}