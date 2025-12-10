package uni.fis.catalogo.Entity.Items;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "item")
@SecondaryTable(
    name = "servicio",
    pkJoinColumns = @PrimaryKeyJoinColumn(name = "id")
)
public class Servicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "id_catalogo", nullable = false)
    private Integer idCatalogo;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "precio", nullable = false)
    private BigDecimal precio;
    
    @Column(name = "fecha", nullable = false)
    private Date fechaCreacion;
    
    @Column(name = "valoracion")
    private BigDecimal valoracion;
    
    @Column(name = "activo", nullable = false)
    private boolean disponible;
    
    @Column(name = "id_multimedia")
    private Integer idMultimedia;
    
    @Column(name = "duracion", table = "servicio", nullable = false, columnDefinition = "INTERVAL")
    private String duracion;
    
    @Column(name = "horario", table = "servicio", nullable = false)
    private LocalTime horario;
}