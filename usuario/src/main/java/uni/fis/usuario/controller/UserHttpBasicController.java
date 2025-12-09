package uni.fis.usuario.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.request.UserRequest;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.TokenResponse;
import uni.fis.usuario.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserHttpBasicController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<ApiResponse<TokenResponse>> create(@RequestBody UserRequest request) {
        try {
            log.info("Creando usuario: " + request.email());
            UserDto userDto = userService.save(request);
            log.info("Usuario creado exitosamente: " + userDto.id());
            return ResponseEntity.ok(ApiResponse.success("Usuario creado exitosamente",
                    new TokenResponse(userDto.id(), userDto.idRol(), userDto.rolName(), userDto.email())));
        } catch (Exception e) {
            return ResponseEntity.status(400).body( // ← 400 para errores de validación
                    ApiResponse.error("Error al crear usuario: " + e.getMessage(), 400));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<Integer>> findIdByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(userDto -> ResponseEntity.ok(
                        ApiResponse.success("Usuario id encontrado", userDto.id())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Usuario no encontrado con email: " + email, 404)));
    }

}
