package uni.fis.contenido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uni.fis.contenido.dto.ApiResponse;
import uni.fis.contenido.dto.ComentarioPuntoInteresDTO;
import uni.fis.contenido.service.ComentarioPuntoInteresService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comentarios-punto")
public class ComentarioPuntoInteresController {

    private final ComentarioPuntoInteresService comentarioService;

    public ComentarioPuntoInteresController(ComentarioPuntoInteresService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ComentarioPuntoInteresDTO>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.success("Listado de comentarios obtenido correctamente", comentarioService.findAll())
        );
    }

    @GetMapping("/punto/{idPunto}")
    public ResponseEntity<ApiResponse<List<ComentarioPuntoInteresDTO>>> getByPunto(@PathVariable Integer idPunto) {
        return ResponseEntity.ok(
                ApiResponse.success("Comentarios del punto obtenidos correctamente", comentarioService.findByPunto(idPunto))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ComentarioPuntoInteresDTO>> create(@RequestBody ComentarioPuntoInteresDTO dto) {
        return ResponseEntity.ok(
                ApiResponse.success("Comentario registrado correctamente", comentarioService.create(dto))
        );
    }
}
