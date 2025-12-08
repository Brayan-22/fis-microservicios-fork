package uni.fis.contenido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uni.fis.contenido.dto.ApiResponse;
import uni.fis.contenido.dto.LikeResponseDTO;
import uni.fis.contenido.service.PublicacionLikeService;

@RestController
@RequestMapping("/api/v1/publicaciones/likes")
public class PublicacionLikeController {

    private final PublicacionLikeService likeService;

    public PublicacionLikeController(PublicacionLikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/{idPublicacion}/like/{idUsuario}")
    public ResponseEntity<ApiResponse<LikeResponseDTO>> darLike(
            @PathVariable Integer idPublicacion,
            @PathVariable Integer idUsuario
    ) {
        LikeResponseDTO dto = likeService.darLike(idPublicacion, idUsuario);

        return ResponseEntity.ok(
                ApiResponse.success("Like registrado correctamente", dto)
        );
    }

    @DeleteMapping("/{idPublicacion}/like/{idUsuario}")
    public ResponseEntity<ApiResponse<LikeResponseDTO>> quitarLike(
            @PathVariable Integer idPublicacion,
            @PathVariable Integer idUsuario
    ) {
        LikeResponseDTO dto = likeService.quitarLike(idPublicacion, idUsuario);

        return ResponseEntity.ok(
                ApiResponse.success("Like eliminado correctamente", dto)
        );
    }

}
