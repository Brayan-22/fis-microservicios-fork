package uni.fis.vehiculo.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.vehiculo.dto.response.api.ApiResponse;
import uni.fis.vehiculo.entities.VehiculoEntity;
import uni.fis.vehiculo.services.VehiculoService;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> registrarVehiculo(@RequestBody VehiculoEntity vehiculoEntity) {
        var vehiculoResponse = vehiculoService.registrarVehiculo(vehiculoEntity);

        if (vehiculoResponse == null) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.error("No se puede agregar el vehiculo", 400));
        }

        return ResponseEntity.ok(
                ApiResponse.success("El vehiculo fue registrado", vehiculoResponse)
        );
    }

    // obtener los vehiculos por id de usuario
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<VehiculoEntity>>> getVehiculosByUserId(@PathVariable Integer id) {
        return Optional.of(ResponseEntity.ok(
                ApiResponse.success("Se obtuvieron correctamente los vehiculos del usuario " + id,
                        vehiculoService.findVehiculosByUserId(id))
        )).orElse(ResponseEntity.status(400).body(
                ApiResponse.error("Hubo algun error al obtener vehiculos", 400)
        ));
    }
}
