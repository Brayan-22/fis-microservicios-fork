package uni.fis.pago.Security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private Integer userId;
    private String username;
    private String role;
}