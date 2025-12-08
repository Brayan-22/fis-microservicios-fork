package uni.fis.contenido.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uni.fis.contenido.dto.ApiResponse;
import uni.fis.contenido.dto.ComentarioResponseDTO;
import uni.fis.contenido.dto.CrearComentarioDTO;
import uni.fis.contenido.entity.ComentarioEntity;
import uni.fis.contenido.service.ComentarioService;
import java.util.List;





@RestController
@RequestMapping("/api/v1/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ComentarioEntity>> crear(
            @RequestBody CrearComentarioDTO dto
    ) {
        ComentarioEntity comentario = comentarioService.crearComentario(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Comentario creado correctamente", comentario)
        );
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<ApiResponse<List<ComentarioResponseDTO>>> listarPorUsuario(
            @PathVariable String usuario
    ) {
        Integer usuarioInt = Integer.parseInt(usuario);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Comentarios listados correctamente",
                        comentarioService.listarPorUsuario(usuarioInt)
                )
        );
    }

    @GetMapping("/publicacion/{idPublicacion}")
    public ResponseEntity<ApiResponse<List<ComentarioResponseDTO>>> listarPorPublicacion(
            @PathVariable Integer idPublicacion
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Comentarios encontrados",
                        comentarioService.listarPorPublicacion(idPublicacion)
                )
        );
    }
}
