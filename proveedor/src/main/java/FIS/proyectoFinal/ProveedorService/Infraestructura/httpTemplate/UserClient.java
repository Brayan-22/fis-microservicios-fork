package FIS.proyectoFinal.ProveedorService.Infraestructura.httpTemplate;

import FIS.proyectoFinal.ProveedorService.Dominio.modelo.responseDTOS.UsuarioDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UserClient(RestTemplate restTemplate,
                      @Value("${user.service.base-url:http://usuario:8080}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public UsuarioDTO getUserById(Integer id) {
        String url = String.format("%s/api/usuarios/%d", baseUrl, id);
        return restTemplate.getForObject(url, UsuarioDTO.class);
    }
}
