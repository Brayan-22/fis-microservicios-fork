package uni.fis.usuario.entity;


//No me gysta mucho hacerlo pero toca salir rapido de esto y no pensar otro microservicio para arquitectura, etc

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Table(name = "rol")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    // Aqui vamos a mapearlo con el usuario:
//    @OneToMany(mappedBy = "rol", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<UsuarioEntity> usuarios;

//    public RolEntity(Integer id, String nombre) {
//        this.id = id;
//        this.nombre = nombre;
//    }
}
