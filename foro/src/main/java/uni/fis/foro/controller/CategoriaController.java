package uni.fis.foro.controller;

import uni.fis.foro.dto.ApiResponse;
import uni.fis.foro.entity.CategoriaEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.foro.service.CategoriaService;



@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaEntity>> crearCategoria(@RequestBody CategoriaEntity dto) {

        CategoriaEntity creada = categoriaService.crearCategoria(dto.getNombre());

        return ResponseEntity.ok(
                ApiResponse.success("Categoría creada correctamente", creada)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaEntity>>> listar() {

        return ResponseEntity.ok(
            ApiResponse.success(
                "Listado de categorías obtenido correctamente",
                categoriaService.listarCategorias()
            )
        );
    }
}
