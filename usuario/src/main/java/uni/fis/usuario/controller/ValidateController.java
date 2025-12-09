package uni.fis.usuario.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uni.fis.usuario.dto.request.UserValidateRequest;
import uni.fis.usuario.dto.response.ApiResponse;
import uni.fis.usuario.dto.response.TokenResponse;
import uni.fis.usuario.error.InvalidCredentialsException;
import uni.fis.usuario.error.UserNotFoundException;
import uni.fis.usuario.service.PasswordService;
import uni.fis.usuario.service.UserService;

@RestController
@RequestMapping("/api/v1/validate")
@RequiredArgsConstructor
@Slf4j
public class ValidateController {

    private final UserService userService;
    private final PasswordService passwordService;

    @PostMapping("/auth/verify")
    public ResponseEntity<ApiResponse<TokenResponse>> validateUser(
            @RequestBody UserValidateRequest userValidateRequest) {
        try {
            log.info("Validando usuario: " + userValidateRequest.email());
            passwordService.validate(userValidateRequest.email(), userValidateRequest.password());
            log.info("Pase la validación");
            var user = userService.findByEmail(userValidateRequest.email())
                    .orElseThrow(() -> UserNotFoundException.byEmail(userValidateRequest.email()));

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Usuario verificado",
                            new TokenResponse(
                                    user.id(),
                                    user.idRol(),
                                    user.rolName(),
                                    user.email())));

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error(e.getMessage(), 404));

        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(e.getMessage(), 401));
        }
    }
}