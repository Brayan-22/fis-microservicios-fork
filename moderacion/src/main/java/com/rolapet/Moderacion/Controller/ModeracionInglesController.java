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
@RequestMapping("/api/moderacion/ingles")
@Validated
@Slf4j
public class ModeracionInglesController {

    private final ModeracionServiceInt moderacionService;

    @Autowired
    public ModeracionInglesController(@Qualifier("moderacionIngles") ModeracionServiceInt moderacionService) {
        this.moderacionService = moderacionService;
    }

    /**
     * Valida contenido en inglés
     * @Valid activa las validaciones de Bean Validation en el DTO
     */
    @PostMapping("/validar")
    public ResponseEntity<ModeracionResponseDTO> validarContenido(
            @Valid @RequestBody ModeracionRequestDTO request) {

        log.info("Validating English content for user: {}", request.getUsuarioId());
        ModeracionResponseDTO response = moderacionService.validarContenido(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas las palabras prohibidas en inglés
     */
    @GetMapping("/palabras")
    public ResponseEntity<List<PalabraProhibida>> listarPalabras() {
        List<PalabraProhibida> palabras = moderacionService.listarTodasLasPalabras();
        return ResponseEntity.ok(palabras);    }


    /**
     * Agrega una nueva palabra prohibida en inglés
     * Ahora usa un DTO con validaciones en lugar de @RequestParam
     */
    @PostMapping("/palabras")
    public ResponseEntity<PalabraProhibida> agregarPalabra(
            @Valid @RequestBody AgregarPalabraDTO request) {

        log.info("Adding prohibited word in English: '{}'", request.getPalabra());
        PalabraProhibida nueva = moderacionService.agregarPalabraProhibida(
                request.getPalabra(),
                request.getDescripcion()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    /**
     * Actualiza una palabra existente
     */
    @PutMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> actualizarPalabra(
            @PathVariable Integer id,
            @Valid @RequestBody AgregarPalabraDTO request) {

        log.info("Updating word with ID: {}", id);
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
        log.info("Deleting word with ID: {}", id);
        moderacionService.eliminarPalabraProhibida(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca una palabra por ID
     */
    @GetMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> buscarPorId(@PathVariable Integer id) {
        log.info("Searching for word with ID: {}", id);
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

    /**
     * Health check del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("English moderation service operational ✓");
    }
}
