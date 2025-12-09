package uni.fis.pago.Service.Interfaces;

import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;

public interface OrdenItemService {
    Integer agregarOrdenItem(OrdenItemRequest OrdenItemRequest);
    void eliminarOrdenItem(Integer idOrdenItem);
    OrdenItemResponse consultarOrdenItem(Integer idOrdenItem);
}
