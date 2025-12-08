package uni.fis.pago.Controller;

import java.math.BigDecimal;

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

import uni.fis.pago.Model.OrdenItemDTO.OrdenItemRequest;
import uni.fis.pago.Model.PagoDTO.PagoRequest;
import uni.fis.pago.Model.PagoDTO.PagoResponse;
import uni.fis.pago.Service.Interfaces.PagoService;

@RestController 
@RequestMapping("api/pago")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;

    @PostMapping("/crearPago")
    public ResponseEntity<Integer> crearPago(@RequestBody PagoRequest pagoRequest) {
        Integer pagoId = pagoService.doPago(pagoRequest);
        return new ResponseEntity<>(pagoId, HttpStatus.CREATED);
    }
    @PostMapping("/{idPago}/agregarProducto")
    public ResponseEntity<String> agregarProducto(@RequestBody OrdenItemRequest ordenItemRequest,
                                                @PathVariable Integer idPago){
        String response = pagoService.agregarProducto(ordenItemRequest, idPago);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("{idPago}/eliminarProducto/{idProducto}")
    public ResponseEntity<String> eliminarProductoById(@PathVariable Integer idProducto){
        String response = pagoService.eliminarProductoById(idProducto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/TerminarPago/{idPago}")
    public ResponseEntity<BigDecimal> terminarPago(@PathVariable Integer idPago) {
        BigDecimal total = pagoService.terminarPago(idPago);
        return new ResponseEntity<>(total, HttpStatus.OK);

    }
    @GetMapping("/ObtenerPago/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Integer id) {
        PagoResponse pago = pagoService.verDetallesPago(id);
        return new ResponseEntity<>(pago, HttpStatus.OK);
    }
}