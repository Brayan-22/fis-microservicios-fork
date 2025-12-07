package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import lombok.Data;

@Data
public class PublicarComentarioRequestDTO {
    private Long proveedorId;
    private String targetType;
    private Long targetId;
    private String comentario;
    private Integer calificacion;
}
