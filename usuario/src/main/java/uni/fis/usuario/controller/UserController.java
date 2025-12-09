package uni.fis.usuario.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;
import uni.fis.usuario.dto.UserDto;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.UserResponse;
import uni.fis.usuario.mapper.UserMapper;
import uni.fis.usuario.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/jwt/users")
@RequiredArgsConstructor
public class UserController {

        private final UserService userService;

        @GetMapping("")
        public ResponseEntity<ApiResponse<List<UserResponse>>> findAll() {
                List<UserDto> userDto = userService.findAll();
                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Usuarios encontrados",
                                                userDto.stream()
                                                                .map(UserMapper::dtoToResponse)
                                                                .toList()));
        }

        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable Integer id) {
                return userService.findById(id)
                                .map(userDto -> ResponseEntity.ok(
                                                ApiResponse.success(
                                                                "Usuario encontrado",
                                                                UserMapper.dtoToResponse(userDto))))
                                .orElseGet(() -> ResponseEntity.status(404).body(
                                                ApiResponse.error("Usuario no encontrado", 404) // ← 404 en lugar de 402
                                ));
        }

        @GetMapping("/me")
        public ResponseEntity<ApiResponse<Map<String, Object>>> me(@AuthenticationPrincipal Claims claims) {

                return ResponseEntity.ok(
                                ApiResponse.success(
                                                "Usuario encontrado",
                                                Map.of(
                                                                "user_id", claims.get("sub"),
                                                                "email", claims.get("email"),
                                                                "rol", claims.get("rol"))));
        }

}