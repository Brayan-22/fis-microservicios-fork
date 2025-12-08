package uni.fis.contenido.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import uni.fis.contenido.dto.ApiResponse;
import uni.fis.contenido.dto.CrearPublicacionDTO;
import uni.fis.contenido.dto.PublicacionResponseDTO;
import uni.fis.contenido.entity.PublicacionEntity;
import uni.fis.contenido.service.PublicacionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;

    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PublicacionEntity>> crear(
            @RequestBody CrearPublicacionDTO dto
    ) {
        PublicacionEntity pub = publicacionService.crearPublicacion(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Publicación creada correctamente", pub)
        );
    }

    @GetMapping("/foro/{idForo}")
    public ResponseEntity<ApiResponse<List<PublicacionResponseDTO>>> listarPorForo(
            @PathVariable Integer idForo
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Publicaciones listadas correctamente",
                        publicacionService.listarPorForo(idForo)
                )
        );
    }

    @GetMapping("/usuario/{usuario}")
    public ResponseEntity<ApiResponse<List<PublicacionResponseDTO>>> listarPorUsuario(
            @PathVariable Integer usuario
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Publicaciones listadas correctamente",
                        publicacionService.listarPorUsuario(usuario)
                )
        );
    }

}
