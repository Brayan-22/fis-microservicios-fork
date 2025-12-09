package com.RolaPet.Geolocalizacion.controller;
import com.RolaPet.Geolocalizacion.domain.dto.*;
import com.RolaPet.Geolocalizacion.service.GeolocalizacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/geolocalizacion")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class GeolocalizacionController {

    private final GeolocalizacionService geolocalizacionService;

    @PostMapping("/geocode")
    public ResponseEntity<ApiResponse<CoordenadaDTO>> geocodificar(
            @Valid @RequestBody GeocodeRequestDTO request) {

        log.info("Geocodificando dirección: {}", request.getDireccion());

        try {
            CoordenadaDTO coordenada = geolocalizacionService.geocode(request);

            log.info("Coordenadas encontradas: ({}, {})",
                    coordenada.getLatitud(), coordenada.getLongitud());

            return ResponseEntity.ok(ApiResponse.success("Geocodificación exitosa", coordenada));
        } catch (Exception e) {
            log.error("Error en geocodificación: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al geocodificar: " + e.getMessage(), 400));
        }
    }

    @PostMapping("/reverse")
    public ResponseEntity<ApiResponse<DireccionDTO>> reverseGeocodificar(
            @Valid @RequestBody ReverseGeocodeRequestDTO request) {

        log.info("Reverse geocoding: ({}, {})",
                request.getLatitud(), request.getLongitud());

        try {
            DireccionDTO direccion = geolocalizacionService.reverseGeocode(request);

            log.info("Dirección encontrada: {}", direccion.getDireccionCompleta());

            return ResponseEntity.ok(ApiResponse.success("Reverse geocoding exitoso", direccion));
        } catch (Exception e) {
            log.error("Error en reverse geocoding: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener dirección: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<CoordenadaDTO>> buscarLugar(
            @RequestParam String query) {

        log.info("Buscando lugar: {}", query);

        try {
            GeocodeRequestDTO request = new GeocodeRequestDTO();
            request.setDireccion(query);

            CoordenadaDTO coordenada = geolocalizacionService.geocode(request);

            return ResponseEntity.ok(ApiResponse.success("Lugar encontrado", coordenada));
        } catch (Exception e) {
            log.error("Error al buscar lugar: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al buscar lugar: " + e.getMessage(), 400));
        }
    }

    @PostMapping("/punto-interes")
    public ResponseEntity<ApiResponse<PuntoInteresDTO>> registrarPuntoInteres(
            @Valid @RequestBody PuntoInteresRequestDTO request,
            @RequestHeader(value = "X-Usuario-Id", required = false) Long adminId) {

        log.info("Registrando punto de interés: {}", request.getNombre());

        try {
            if (adminId == null) {
                adminId = 1L;
            }

            PuntoInteresDTO punto = geolocalizacionService.registrarPuntoInteres(request);

            log.info("Punto registrado con ID: {}", punto.getId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Punto de interés registrado exitosamente", punto));
        } catch (Exception e) {
            log.error("Error al registrar punto: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al registrar punto: " + e.getMessage(), 400));
        }
    }

    @PutMapping("/punto-interes/{id}")
    public ResponseEntity<ApiResponse<PuntoInteresDTO>> actualizarPuntoInteres(
            @PathVariable Integer id,
            @Valid @RequestBody PuntoInteresRequestDTO request) {

        log.info("Actualizando punto de interés ID: {}", id);

        try {
            PuntoInteresDTO punto = geolocalizacionService.actualizarPuntoInteres(id, request);

            log.info("Punto actualizado exitosamente");

            return ResponseEntity.ok(ApiResponse.success("Punto actualizado exitosamente", punto));
        } catch (Exception e) {
            log.error("Error al actualizar punto: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al actualizar punto: " + e.getMessage(), 400));
        }
    }

    @DeleteMapping("/punto-interes/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarPuntoInteres(@PathVariable Integer id) {

        log.info("Eliminando punto de interés ID: {}", id);

        try {
            geolocalizacionService.eliminarPuntoInteres(id);

            log.info("Punto eliminado exitosamente");

            return ResponseEntity.ok(ApiResponse.success("Punto eliminado exitosamente"));
        } catch (Exception e) {
            log.error("Error al eliminar punto: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al eliminar punto: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/punto-interes/{id}")
    public ResponseEntity<ApiResponse<PuntoInteresDTO>> obtenerPuntoInteres(@PathVariable Integer id) {

        log.info("Consultando punto de interés ID: {}", id);

        try {
            PuntoInteresDTO punto = geolocalizacionService.obtenerPuntoInteresPorId(id);

            return ResponseEntity.ok(ApiResponse.success("Punto encontrado", punto));
        } catch (Exception e) {
            log.error("Error al obtener punto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Punto no encontrado: " + e.getMessage(), 404));
        }
    }

    @GetMapping("/puntos-interes")
    public ResponseEntity<ApiResponse<List<PuntoInteresDTO>>> listarPuntosInteres() {

        log.info("Listando todos los puntos de interés");

        try {
            List<PuntoInteresDTO> puntos = geolocalizacionService.listarPuntosInteres();

            return ResponseEntity.ok(ApiResponse.success("Puntos listados exitosamente", puntos));
        } catch (Exception e) {
            log.error("Error al listar puntos: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al listar puntos: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/puntos-interes/buscar")
    public ResponseEntity<ApiResponse<List<PuntoInteresDTO>>> buscarPuntosPorNombre(
            @RequestParam String nombre) {

        log.info("Buscando puntos por nombre: {}", nombre);

        try {
            List<PuntoInteresDTO> puntos = geolocalizacionService.buscarPorNombre(nombre);

            return ResponseEntity.ok(ApiResponse.success("Búsqueda completada", puntos));
        } catch (Exception e) {
            log.error("Error en búsqueda: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en búsqueda: " + e.getMessage(), 400));
        }
    }

    @PostMapping("/direccion")
    public ResponseEntity<ApiResponse<DireccionDTO>> crearDireccion(
            @Valid @RequestBody DireccionDTO request) {

        log.info("Creando dirección");

        try {
            DireccionDTO direccion = geolocalizacionService.crearDireccion(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Dirección creada exitosamente", direccion));
        } catch (Exception e) {
            log.error("Error al crear dirección: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al crear dirección: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/direccion/{id}")
    public ResponseEntity<ApiResponse<DireccionDTO>> obtenerDireccion(@PathVariable Integer id) {

        log.info("Consultando dirección ID: {}", id);

        try {
            DireccionDTO direccion = geolocalizacionService.obtenerDireccionPorId(id);

            return ResponseEntity.ok(ApiResponse.success("Dirección encontrada", direccion));
        } catch (Exception e) {
            log.error("Error al obtener dirección: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Dirección no encontrada: " + e.getMessage(), 404));
        }
    }
}