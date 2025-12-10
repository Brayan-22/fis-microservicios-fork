package uni.fis.catalogo.Entity.Items;

import java.math.BigDecimal;
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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "item")
@SecondaryTable(
    name = "producto",
    pkJoinColumns = @PrimaryKeyJoinColumn(name = "id")
)
public class Producto {
    
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
    
    @Column(name = "cantidad", table = "producto", nullable = false)
    private Integer cantidad;
    
    @Column(name = "tamano", table = "producto", nullable = false)
    private String tamano;
    
    @Column(name = "peso", table = "producto", nullable = false)
    private BigDecimal peso;
    
    @Column(name = "id_color", table = "producto", nullable = false)
    private Integer  id_color;

    @Column(name = "id_unidad_peso", table = "producto", nullable = false)
    private Integer id_unidad_peso;
}