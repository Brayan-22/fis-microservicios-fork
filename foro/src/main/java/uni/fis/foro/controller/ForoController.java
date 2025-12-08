package uni.fis.foro.controller;

import uni.fis.foro.entity.ForoEntity;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.foro.service.ForoService;
import uni.fis.foro.dto.ApiResponse;
import uni.fis.foro.dto.CrearForoDTO;


@RestController
@RequestMapping("/api/foros")
public class ForoController {

    private final ForoService foroService;

    public ForoController(ForoService foroService) {
        this.foroService = foroService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ForoEntity>> crearForo(@RequestBody CrearForoDTO dto) {

        ForoEntity foro = foroService.crearForo(dto.getNombre());

        return ResponseEntity.ok(
                ApiResponse.success("Foro creado correctamente", foro)
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ForoEntity>>> listar() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Listado de foros obtenido correctamente",
                        foroService.listarForos()
                )
        );
    }

    @GetMapping("/filtrar")
    public ResponseEntity<ApiResponse<List<ForoEntity>>> filtrar(@RequestParam String nombre) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resultado del filtro obtenido correctamente",
                        foroService.filtrarForos(nombre)
                )
        );
    }
}
