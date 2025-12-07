package uni.fis.catalogo.Model.ItemDto;


import java.math.BigDecimal;

import lombok.Data;

@Data

public class ProductoRequest {
    private String nombre;
    private BigDecimal precio;
    private int cantidad;
    private String tamaño;
    private BigDecimal peso;
    private Integer id_color;
    private Integer id_unidad_peso;
}