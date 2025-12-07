package com.rolapet.Moderacion.Controller;

import com.rolapet.Moderacion.Domain.dto.ModeracionRequestDTO;
import com.rolapet.Moderacion.Domain.dto.ModeracionResponseDTO;
import com.rolapet.Moderacion.Domain.entity.PalabraProhibida;
import com.rolapet.Moderacion.Service.ModeracionServiceInt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para moderación de contenido en ESPAÑOL
 */
@RestController
@RequestMapping("/api/moderacion/espanol")
@Slf4j
public class ModeracionEspanolController {

    private final ModeracionServiceInt moderacionService;

    @Autowired
    public ModeracionEspanolController(@Qualifier("moderacionEspanol") ModeracionServiceInt moderacionService) {
        this.moderacionService = moderacionService;
    }

    /**
     * Valida contenido en español
     */
    @PostMapping("/validar")
    public ResponseEntity<ModeracionResponseDTO> validarContenido(@RequestBody ModeracionRequestDTO request) {
        log.info("Validando contenido en español para usuario: {}", request.getUsuarioId());
        ModeracionResponseDTO response = moderacionService.validarContenido(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todas las palabras prohibidas en español
     */
    @GetMapping("/palabras")
    public ResponseEntity<List<PalabraProhibida>> listarPalabras() {
        return ResponseEntity.ok(moderacionService.listarTodasLasPalabras());
    }

    /**
     * Lista solo palabras activas en español
     */
    @GetMapping("/palabras/activas")
    public ResponseEntity<List<PalabraProhibida>> listarPalabrasActivas() {
        return ResponseEntity.ok(moderacionService.listarPalabrasActivas());
    }

    /**
     * Agrega una nueva palabra prohibida en español
     */
    @PostMapping("/palabras")
    public ResponseEntity<PalabraProhibida> agregarPalabra(
            @RequestParam String palabra,
            @RequestParam String descripcion) {
        try {
            PalabraProhibida nueva = moderacionService.agregarPalabraProhibida(palabra, descripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (IllegalArgumentException e) {
            log.error("Error al agregar palabra: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza una palabra existente
     */
    @PutMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> actualizarPalabra(
            @PathVariable Integer id,
            @RequestParam String palabra,
            @RequestParam String descripcion) {
        try {
            PalabraProhibida actualizada = moderacionService.actualizarPalabraProhibida(id, palabra, descripcion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Elimina una palabra
     */
    @DeleteMapping("/palabras/{id}")
    public ResponseEntity<Void> eliminarPalabra(@PathVariable Integer id) {
        try {
            moderacionService.eliminarPalabraProhibida(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca una palabra por ID
     */
    @GetMapping("/palabras/{id}")
    public ResponseEntity<PalabraProhibida> buscarPorId(@PathVariable Integer id) {
        try {
            PalabraProhibida palabra = moderacionService.buscarPorId(id);
            return ResponseEntity.ok(palabra);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Endpoint informativo del idioma
     */
    @GetMapping("/idioma")
    public ResponseEntity<String> getIdioma() {
        return ResponseEntity.ok(moderacionService.getIdioma());
    }
}