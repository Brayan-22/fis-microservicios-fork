package uni.fis.pago.Service.Imp;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;
import uni.fis.pago.Entity.Pago;
import uni.fis.pago.Exceptions.Exceptions;
import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraRequest;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;
import uni.fis.pago.Repository.OrdenCompraRepository;
import uni.fis.pago.Repository.PagoRepository;
import uni.fis.pago.Service.Interfaces.OrdenCompraService;
import uni.fis.pago.Service.Interfaces.OrdenItemService;
import uni.fis.pago.Service.Interfaces.PagoService;


@Service
@Log4j2
public class PagoServiceImp implements PagoService{
    @Autowired
    PagoRepository pagoRepository;
    @Autowired
    OrdenItemService ordenItemService;
    @Autowired
    OrdenCompraService ordenCompraService;
    @Autowired
    OrdenCompraRepository repositorioCompras;
    @Override
    public Integer doPago(PagoRequest pagoRequest){
        log.info("Guardando información del pago: {}" + pagoRequest);

        Pago pago = Pago.builder()
                    .fecha(new Date())
                    .monto_total(new BigDecimal(0))
                    .idUsuario(pagoRequest.getId_usuario())
                    .idMetodoPago(pagoRequest.getId_metodo_pago())
                    .build();
        log.info("pago con el id "+pago.getId()+" ha sido procesado");
        pagoRepository.save(pago);
        return pago.getId();
    }
    @Override
    public String agregarProducto(OrdenItemRequest ordenItemRequest, Integer idPago){
        Integer idItem = ordenItemService.agregarOrdenItem(ordenItemRequest);
        Integer idCompra = ordenCompraService.crearOrdenCompra(OrdenCompraRequest.builder().idPago(idPago).idOrdenItem(idItem).build());
        return "Se agrego exitosamente el producto con el id "+ idItem + " asociado a la orden con el id " + idCompra;
    }
    @Transactional
    @Override
    public String eliminarProductoById(Integer idItem){
        repositorioCompras.deleteByIdOrdenItem(idItem);
        ordenItemService.eliminarOrdenItem(idItem);
        return "Item con el id "+idItem+" eliminado exitosamente del pago";
    }
    @Override
    public BigDecimal terminarPago(Integer idPago){
        Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(()-> new Exceptions("El pago con id " + idPago + "no fue encontrado", "PAGO_NOT_FOUND"));
        BigDecimal total = ordenCompraService.calcularMontoTotal(idPago);
        pago.setMonto_total(total);
        pagoRepository.save(pago);
        return total;
    }
    @Override
    public PagoResponse verDetallesPago(Integer idPago){
        Pago pago = pagoRepository.findById(idPago)
        .orElseThrow(()-> new Exceptions("El pago con id " + idPago + "no fue encontrado", "PAGO_NOT_FOUND"));
        PagoResponse response = PagoResponse.builder()
                                .id(pago.getId())
                                .fecha(pago.getFecha())
                                .monto_total(pago.getMonto_total())
                                .id_usuario(pago.getIdUsuario())
                                .id_metodo_pago(pago.getIdMetodoPago())
                                .build();
        return response;
    }
}
