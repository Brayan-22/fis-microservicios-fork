package uni.fis.foro.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uni.fis.foro.dto.ApiResponse;
import uni.fis.foro.dto.AsignarCategoriaForoDTO;
import uni.fis.foro.entity.ForoCategoriaEntity;
import uni.fis.foro.service.ForoCategoriaService;
import java.util.List;



@RestController
@RequestMapping("/api/foro-categorias")
public class ForoCategoriaController {

    private final ForoCategoriaService foroCategoriaService;

    public ForoCategoriaController(ForoCategoriaService foroCategoriaService) {
        this.foroCategoriaService = foroCategoriaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ForoCategoriaEntity>> asignar(@RequestBody AsignarCategoriaForoDTO dto) {

        ForoCategoriaEntity asignada = foroCategoriaService.asignarCategoria(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Categoría asignada correctamente al foro", asignada)
        );
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<ApiResponse<List<ForoCategoriaEntity>>> listarPorCategoria(
            @PathVariable Integer idCategoria
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Foros obtenidos por categoría",
                        foroCategoriaService.listarPorCategoria(idCategoria)
                )
        );
    }
}
