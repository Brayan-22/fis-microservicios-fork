package FIS.proyectoFinal.ProveedorService.Infraestructura.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private Integer userId;
    private String username;
    private String role;
    private String token;
}
