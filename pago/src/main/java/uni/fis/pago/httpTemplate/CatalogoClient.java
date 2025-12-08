package uni.fis.pago.httpTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import uni.fis.pago.Model.ItemDto.ProductoResponse;

@Component
public class CatalogoClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${catalogo.service.url}")
    private String catalogoBaseUrl;

    public ProductoResponse obtenerProductoPorId(Integer productoId){
        try {
            String url = String.format("%s/api/catalogo/%d/producto/%d", catalogoBaseUrl, 1, productoId);
            return restTemplate.getForObject(url, ProductoResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
