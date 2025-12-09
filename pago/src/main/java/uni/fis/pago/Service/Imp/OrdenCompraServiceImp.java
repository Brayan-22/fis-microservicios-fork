package uni.fis.pago.Service.Imp;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import uni.fis.pago.Entity.OrdenCompra;
import uni.fis.pago.Entity.OrdenItem;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraRequest;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;
import uni.fis.pago.Repository.OrdenCompraRepository;
import uni.fis.pago.Repository.OrdenItemRepository;
import uni.fis.pago.Service.Interfaces.OrdenCompraService;

@Service
@Log4j2

public class OrdenCompraServiceImp implements OrdenCompraService{
    @Autowired
    OrdenCompraRepository ordenCompraRepository;
    @Autowired
    OrdenItemRepository ordenItemRepository;

    @Override
    public Integer crearOrdenCompra(OrdenCompraRequest ordenCompraRequest){
        OrdenCompra ordenCompra = OrdenCompra.builder()
                                    .fecha(new Date())
                                    .idPago(ordenCompraRequest.getIdPago())
                                    .idOrdenItem(ordenCompraRequest.getIdOrdenItem())
                                .build();
        log.info("Procesando la información de la orden de compra");
        ordenCompraRepository.save(ordenCompra);
        log.info("Orden de compra recibida y aprobada");
        return ordenCompra.getId();
    }
    @Override
    public void eliminarOrdenCompra(Integer idOrdenCompra){
        log.info("Verificando la orden de compra con el id " + idOrdenCompra);
        if(!ordenCompraRepository.existsById(idOrdenCompra)){
            throw new Exceptions("La Orden de compra con el id " + idOrdenCompra +" no existe","ORDEN_COMPRA_NOT_FOUND");
        }
        ordenCompraRepository.deleteById(idOrdenCompra);
        log.info("Orden de compra eliminada exitosamente!");
    }
    @Override
    public OrdenCompraResponse consultarOrdenCompra(Integer idOrdenCompra){
        log.info("Buscando la orden de compra con el id "+idOrdenCompra);
        OrdenCompra ordenCompra = ordenCompraRepository.findById(idOrdenCompra)
                            .orElseThrow(()-> new Exceptions("La Orden de compra con el id " + idOrdenCompra +" no existe","ORDEN_COMPRA_NOT_FOUND"));
        OrdenCompraResponse response = OrdenCompraResponse.builder()
                                        .id(ordenCompra.getId())
                                        .fecha(ordenCompra.getFecha())
                                        .idPago(ordenCompra.getIdPago())
                                        .idOrdenItem(ordenCompra.getIdOrdenItem())
                                    .build();   
        log.info("Orden de compra encontrada exitosamente!");                 
        return response;
    }
    @Override
    public BigDecimal calcularMontoTotal(Integer idPago){
        OrdenCompra[] ordenesCompras = ordenCompraRepository.findByIdPago(idPago);
        BigDecimal total = new BigDecimal(0);
        for(OrdenCompra ordenCompra : ordenesCompras){
            OrdenItem ordenItem = ordenItemRepository.findById(ordenCompra.getIdOrdenItem())
            .orElseThrow(()-> new Exceptions("La Orden de compra con el id " + ordenCompra.getIdOrdenItem() +" no existe","ORDEN_ITEM_NOT_FOUND"));
            total = total.add(ordenItem.getSubtotal());
        }
        return total;
    }
}
