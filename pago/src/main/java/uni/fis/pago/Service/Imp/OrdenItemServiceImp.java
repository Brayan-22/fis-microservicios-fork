package uni.fis.pago.Service.Imp;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Model.ItemDto.ProductoResponse;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Service.Interfaces.OrdenItemService;
import uni.fis.pago.httpTemplate.CatalogoClient;

@Service
public class OrdenItemServiceImp implements OrdenItemService{
    @Autowired 
    OrdenItemRepository ordenItemRepository;

    @Autowired
    CatalogoClient catalogoClient;
    @Override
    public Integer agregarOrdenItem(OrdenItemRequest ordenItemRequest){
        // Obtener precio del catálogo y calcular subtotal
        ProductoResponse producto = catalogoClient.obtenerProductoPorId(ordenItemRequest.getId_item());
        BigDecimal valorUnitario = null;
        if (producto != null) {
            valorUnitario = producto.getPrecio();
        }
        Integer cantidad = ordenItemRequest.getCantidad();
        BigDecimal subtotal = null;
        if (valorUnitario != null && cantidad != null) {
            subtotal = valorUnitario.multiply(new BigDecimal(cantidad));
        }

        OrdenItem ordenItem = OrdenItem.builder()
                            .idItem(ordenItemRequest.getId_item())
                            .cantidad(cantidad)
                            .valor_unitario(valorUnitario)
                            .subtotal(subtotal)
                            .build();
        ordenItemRepository.save(ordenItem);
        return ordenItem.getId();
    }
    @Override
    public void eliminarOrdenItem(Integer id){
        if(!ordenItemRepository.existsById(id)){
            throw new Exceptions("Orden de item con el id "+ id +" no fue encontrado", "ORDEN_ITEM_NOT_FOUND" );
        }
        ordenItemRepository.deleteById(id);
    }
    @Override
    public OrdenItemResponse consultarOrdenItem(Integer idOrdenItem){
        OrdenItem ordenItem = ordenItemRepository.findById(idOrdenItem).
        orElseThrow(() -> new Exceptions("Orden de item con el id "+ idOrdenItem +" no fue encontrado", "ORDEN_ITEM_NOT_FOUND"));
        OrdenItemResponse ordenItemResponse = OrdenItemResponse.builder()
                                              .id(ordenItem.getId())
                                              .id_item(ordenItem.getIdItem())
                                              .cantidad(ordenItem.getCantidad())
                                              .valor_unitario(ordenItem.getValor_unitario())
                                              .subtotal(ordenItem.getSubtotal())
                                              .id_orden_compra(null)
                                              .build();
        return ordenItemResponse;
    }

}
