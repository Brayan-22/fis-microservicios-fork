package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import lombok.Data;

@Data

public class CrearProveedorDTO {
    private String nombre;
    private String documento;
    private String correoContacto;
    private String direccion;
    private String categoria;
}
