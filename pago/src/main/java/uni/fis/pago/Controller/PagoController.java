package uni.fis.pago.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.fis.pago.Model.OrdenCompraDTO.OrdenCompraResponse;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.OrdenItemDTO.OrdenItemResponse;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;
import uni.fis.pago.Service.Interfaces.OrdenCompraService;
import uni.fis.pago.Service.Interfaces.OrdenItemService;
import uni.fis.pago.Service.Interfaces.PagoService;

@RestController 
@RequestMapping("api/pago")
public class PagoController{
    @Autowired
    OrdenCompraService ordenCompraService;
    @Autowired
    OrdenItemService ordenItemService;
    @Autowired
    PagoService pagoService;
    @PostMapping("/crearPago")
    public ResponseEntity<Integer> crearEntityPago(@RequestBody PagoRequest pagoRequest) {
        Integer pagoId = pagoService.doPago(pagoRequest);
        return new ResponseEntity<>(pagoId, HttpStatus.CREATED);
    }
    @GetMapping("/ObtenerPago/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Integer id){
        PagoResponse pago = pagoService.verDetallesPago(id);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }
    @PostMapping("/crearOrdenCompra")
    public ResponseEntity<Integer> crearEntityOrdenCompra(@RequestBody Integer idPago){
        Integer ordenCompraId = ordenCompraService.crearOrdenCompra(idPago);
        return new ResponseEntity<>(ordenCompraId, HttpStatus.CREATED);
    }
    @GetMapping("/ObtenerOrdenCompra/{id}")
    public ResponseEntity<OrdenCompraResponse> obtenerOrdenCompraPorId(@PathVariable Integer id){
        OrdenCompraResponse response = ordenCompraService.consultarOrdenCompra(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/crearOrdenItem")
    public ResponseEntity<Integer> crearEntityOrdenItem(@RequestBody OrdenItemRequest ordenItemRequest){
        Integer ordenItemId = ordenItemService.agregarOrdenItem(ordenItemRequest);
        return new ResponseEntity<>(ordenItemId, HttpStatus.CREATED);
    }
    @GetMapping("/ObtenerOrdenItem/{idOrdenItem}")
    public ResponseEntity<OrdenItemResponse> obtenerOrdenItemPorId(@PathVariable Integer idOrdenItem){
        OrdenItemResponse response = ordenItemService.consultarOrdenItem(idOrdenItem);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/EliminarOrdenCompra/{idOrdenCompra}")
    public ResponseEntity<String> eliminarOrdenCompraPorId(@PathVariable Integer idOrdenCompra){
        ordenCompraService.eliminarOrdenCompra(idOrdenCompra);
        return new ResponseEntity<>("Orden de compra eliminada exitosamente!", HttpStatus.OK);
    }
    @DeleteMapping("/EliminarOrdenItem/{idOrdenItem}")
    public ResponseEntity<String> eliminarOrdenItemPorId(@PathVariable Integer idOrdenItem){
        ordenItemService.eliminarOrdenItem(idOrdenItem);
        return new ResponseEntity<>("Orden de item eliminada exitosamente!", HttpStatus.OK);
    }
}