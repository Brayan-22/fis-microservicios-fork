package fis.auth.infrastructure.controller;


import fis.auth.application.service.LoginService;
import fis.auth.application.service.SignInService;
import fis.auth.domain.model.Login;
import fis.auth.domain.model.SignIn;
import fis.auth.domain.model.Token;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.dto.request.RefreshRequest;
import fis.auth.infrastructure.dto.request.SignInRequest;
import fis.auth.infrastructure.dto.response.api.ApiResponse;
import fis.auth.infrastructure.mapper.AuthMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    private final LoginService loginService;
    private final SignInService signInService;

    public AuthController(
            LoginService loginService,
            SignInService signInService
    ) {
        this.loginService = loginService;
        this.signInService = signInService;
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse<Token>> login(@RequestBody LoginRequest login) {
        Login loginDomain = AuthMapper.toDomain(login);
        Token token = loginService.execute(loginDomain);

        return ResponseEntity.ok()
                .body(ApiResponse.success("Inicio de sesión exitoso", token));
    }

    @PostMapping("sign-in")
    public ResponseEntity<ApiResponse<Token>> signIn(@RequestBody SignInRequest signIn) {
        log.info("Auth: request enviada {}", signIn);
        SignIn signInDomain = AuthMapper.toDomain(signIn);
        Token token = signInService.execute(signInDomain); // Igual aquí

        return ResponseEntity.ok()
                .body(ApiResponse.success("Registro exitoso", token));
    }

    @PostMapping("refresh")
    public ResponseEntity<ApiResponse<Token>> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok()
                .body(ApiResponse.success("Token exitoso", loginService.refresh(request.refreshToken())));
    }

}
