package uni.fis.multimedia.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import uni.fis.multimedia.dto.MultimediaResponseDTO;
import uni.fis.multimedia.entity.MultimediaEntity;
import uni.fis.multimedia.service.MultimediaService;

import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/multimedia")
public class MultimediaController {

    private final MultimediaService multimediaService;

    public MultimediaController(MultimediaService multimediaService) {
        this.multimediaService = multimediaService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MultimediaEntity> crear(@RequestParam("archivo") MultipartFile archivo) throws IOException {
        return ResponseEntity.ok(multimediaService.guardarArchivo(archivo));
    }

    @GetMapping
    public ResponseEntity<List<MultimediaEntity>> listar() {
        return ResponseEntity.ok(multimediaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultimediaResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(multimediaService.obtenerImagen(id));
    }
}