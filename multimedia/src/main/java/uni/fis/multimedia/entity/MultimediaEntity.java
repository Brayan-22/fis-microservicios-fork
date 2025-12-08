package uni.fis.multimedia.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;



@Entity
@Data
@Table(name = "multimedia")
public class MultimediaEntity {
    @Id 
    @Column(name = "id_multi")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Column(name = "tipo_archivo")
    private String tipoArchivo;

}