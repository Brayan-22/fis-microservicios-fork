package uni.fis.usuario.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido_1;
    private String apellido_2;
    private LocalDateTime fechaDeNacimiento;

    @OneToOne
    @JoinColumn(name = "id_documento")
    private DocumentoEntity idDocumento; // foranea

    private String email;
    private Integer strikes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol")
    @JsonIgnore
    private RolEntity rol;
    private Integer imgPerfil;


}