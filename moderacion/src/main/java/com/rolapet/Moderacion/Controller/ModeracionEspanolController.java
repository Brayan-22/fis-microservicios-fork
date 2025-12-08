package com.rolapet.Moderacion.Controller;

import com.rolapet.Moderacion.Domain.dto.AgregarPalabraDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Service.ModeracionServiceInt;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/moderacion/espanol")
@Validated
@Slf4j
public class ModeracionEspanolController {

    private final ModeracionServiceInt moderacionService;

    @Autowired
    public ModeracionEspanolController(@Qualifier("moderacionEspanol") ModeracionServiceInt moderacionService) {
        this.moderacionService = moderacionService;
    }

    @PostMapping("/validar")
    public ResponseEntity<ModeracionResponseDTO> validarContenido(
            @Valid @RequestBody ModeracionRequestDTO request) {

        log.info("Validando contenido en español para usuario: {}", request.getUsuarioId());
        ModeracionResponseDTO response = moderacionService.validarContenido(request);
        return ResponseEntity.ok(response);
    }
    /**
     * Lista todas las palabras prohibidas en español
     */
    @GetMapping("/palabras")
    public ResponseEntity<List<PalabraProhibida>> listarPalabras() {
        List<PalabraProhibida> palabras = moderacionService.listarTodasLasPalabras();
        return ResponseEntity.ok(palabras);
    }

    /**
     * Agregar palabras
     */
    @PostMapping("/palabras")
    public ResponseEntity<PalabraProhibida> agregarPalabra(
            @Valid @RequestBody AgregarPalabraDTO request) {

        log.info("Agregando palabra prohibida en español: '{}'", request.getPalabra());
        PalabraProhibida nueva = moderacionService.agregarPalabraProhibida(
                request.getPalabra(),
                request.getDescripcion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }
    @PutMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> actualizarPalabra(
            @PathVariable Integer id,
            @Valid @RequestBody AgregarPalabraDTO request) {

        log.info("Actualizando palabra con ID: {}", id);
        PalabraProhibida actualizada = moderacionService.actualizarPalabraProhibida(
                id,
                request.getPalabra(),
                request.getDescripcion()
        );
        return ResponseEntity.ok(actualizada);
    }

    /**
     * Elimina una palabra
     */
    @DeleteMapping("/palabras/{id}")
    public ResponseEntity<Void> eliminarPalabra(@PathVariable Integer id) {
        log.info("Eliminando palabra con ID: {}", id);
        moderacionService.eliminarPalabraProhibida(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Busca una palabra por ID
     */
    @GetMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> buscarPorId(@PathVariable Integer id) {
        log.info("Buscando palabra con ID: {}", id);
        PalabraProhibida palabra = moderacionService.buscarPorId(id);
        return ResponseEntity.ok(palabra);
    }

    /**
     * Endpoint informativo del idioma
     */
    @GetMapping("/idioma")
    public ResponseEntity<String> getIdioma() {
        return ResponseEntity.ok(moderacionService.getIdioma());
    }
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio de moderación en español operativo ✓");
    }


}