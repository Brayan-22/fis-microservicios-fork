package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import lombok.Data;

@Data
public class PublicarComentarioRequestDTO {
    private Integer proveedorId;
    private String targetType;
    private Integer targetId;
    private String comentario;
    private Integer calificacion;
}
