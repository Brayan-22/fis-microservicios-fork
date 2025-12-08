package uni.fis.pago.Service.Interfaces;

import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;

public interface OrdenCompraService {
    Integer crearOrdenCompra(Integer idPago);
    void eliminarOrdenCompra(Integer idOrdenCompra);
    OrdenCompraResponse consultarOrdenCompra(Integer idOrdenCompra);
}
