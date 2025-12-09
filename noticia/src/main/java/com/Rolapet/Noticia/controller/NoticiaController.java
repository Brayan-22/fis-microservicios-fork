package com.Rolapet.Noticia.controller;

import com.Rolapet.Noticia.domain.dto.ActualizarNoticiaRequest;
import com.Rolapet.Noticia.domain.dto.ApiResponse;
import com.Rolapet.Noticia.domain.dto.CrearNoticiaRequest;
import com.Rolapet.Noticia.domain.dto.NoticiaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final com.Rolapet.Noticia.service.NoticiaService noticiaService;

    /**
     * RF41: Listar noticias
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<NoticiaDTO>>> listarNoticias(
            @RequestParam(defaultValue = "publicado") String estado) {

        List<NoticiaDTO> noticias = noticiaService.listarNoticias(estado);

        return ResponseEntity.ok(
                new ApiResponse<>(true, null, noticias)
        );
    }

    /**
     * Obtener noticia por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticiaDTO>> obtenerPorId(@PathVariable Integer id) {
        try {
            NoticiaDTO noticia = noticiaService.obtenerPorId(id);
            return ResponseEntity.ok(new ApiResponse<>(true, null, noticia));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * RF42: Crear noticia (solo administradores)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<NoticiaDTO>> crear(
            @Valid @RequestBody CrearNoticiaRequest request) {

        Integer autorId = 1; // Placeholder - obtener del usuario autenticado

        NoticiaDTO noticia = noticiaService.crear(request, autorId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Noticia creada exitosamente", noticia));
    }

    /**
     * RF42: Actualizar noticia (solo administradores)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NoticiaDTO>> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarNoticiaRequest request) {

        try {
            Integer usuarioId = 1; // Placeholder
            NoticiaDTO noticia = noticiaService.actualizar(id, request, usuarioId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Noticia actualizada exitosamente", noticia)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    /**
     * Eliminar noticia (solo administradores)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(
            @PathVariable Integer id) {

        try {
            Integer usuarioId = 1; // Placeholder
            noticiaService.eliminar(id, usuarioId);

            return ResponseEntity.ok(
                    new ApiResponse<>(true, "Noticia eliminada exitosamente")
            );
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}
