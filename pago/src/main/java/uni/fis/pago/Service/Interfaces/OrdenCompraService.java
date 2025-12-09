package uni.fis.pago.Service.Interfaces;

import java.math.BigDecimal;

import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraRequest;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;

public interface OrdenCompraService {
    Integer crearOrdenCompra(OrdenCompraRequest ordenCompraRequest);
    void eliminarOrdenCompra(Integer idOrdenCompra);
    OrdenCompraResponse consultarOrdenCompra(Integer idOrdenCompra);
    public BigDecimal calcularMontoTotal(Integer idPago);
}
