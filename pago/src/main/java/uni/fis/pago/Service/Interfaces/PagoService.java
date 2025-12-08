package uni.fis.pago.Service.Interfaces;

import java.math.BigDecimal;

import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;

public interface PagoService {
    Integer doPago(PagoRequest pagoRequest);
    PagoResponse verDetallesPago(Integer idPago);
    String agregarProducto(OrdenItemRequest ordenItemRequest, Integer idPago);
    String eliminarProductoById(Integer idItem);
    BigDecimal terminarPago(Integer idPago);
}
