package FIS.proyectoFinal.ProveedorService.Dominio.modelo.requestsDTOS;

import lombok.Data;

@Data
public class DarBajoServicioRequestDTO {
    private Integer proveedorId;
    private Integer catalogoId;
    private Integer servicioId;
    private String motivo;
}
