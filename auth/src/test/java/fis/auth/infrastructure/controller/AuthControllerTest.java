package fis.auth.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fis.auth.application.service.LoginService;
import fis.auth.application.service.SignInService;
import fis.auth.domain.model.Token;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.error.NoUserFoundError;
import fis.auth.infrastructure.error.global.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @MockBean
    private SignInService signInService;

        @MockBean
        private fis.auth.infrastructure.service.security.auth.JwtAuthenticationFilter jwtAuthenticationFilter;

        // Register a TestWatcher extension that prints a message to console
        // for each test success/failure. This ensures every test emits a
        // human-readable status line in the surefire console output.
        @RegisterExtension
        static TestWatcher watcher = new TestWatcher() {
                @Override
                public void testSuccessful(ExtensionContext context) {
                        System.out.println("[TEST PASSED] " + context.getDisplayName());
                }

                @Override
                public void testFailed(ExtensionContext context, Throwable cause) {
                        System.out.println("[TEST FAILED] " + context.getDisplayName() + " - " + cause.getMessage());
                }
        };

        private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("login - debe responder 200 con el token cuando las credenciales son correctas")
    void loginShouldReturnTokenWhenCredentialsAreValid() throws Exception {
        // Arrange: simulamos un token válido emitido por el servicio
        Token token = new Token(
                "access-token",
                "refresh-token",
                Instant.parse("2024-01-01T00:00:00Z"),
                true
        );
        when(loginService.execute(ArgumentMatchers.any())).thenReturn(token);

        LoginRequest request = new LoginRequest("usuario@dominio.com", "Clave123!");

        // Act & Assert: el controlador debe devolver 200, cuerpo success y el payload esperado
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("Inicio de sesión exitoso")))
                .andExpect(jsonPath("$.code", is(200)))
                .andExpect(jsonPath("$.data.accessToken", is("access-token")))
                .andExpect(jsonPath("$.data.refreshToken", is("refresh-token")))
                .andExpect(jsonPath("$.data.isValid", is(true)));

        // Assert extra: verificamos que el servicio recibió los datos correctos
        verify(loginService).execute(argThat(login ->
                "usuario@dominio.com".equals(login.email()) &&
                        "Clave123!".equals(login.password())
        ));
    }

    @Test
    @DisplayName("login - debe responder 404 cuando el usuario no existe")
    void loginShouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        // Arrange: el servicio propaga un error de dominio para usuario inexistente
        when(loginService.execute(ArgumentMatchers.any()))
                .thenThrow(NoUserFoundError.of("Usuario no encontrado"));

        LoginRequest request = new LoginRequest("noexiste@mail.com", "Clave123!");

        // Act & Assert: el handler global convierte la excepción en HTTP 404
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Auth: Usuario no encontrado")))
                .andExpect(jsonPath("$.code", is(404)));
    }

    @Test
    @DisplayName("login - debe responder 401 cuando las credenciales son inválidas")
    void loginShouldReturnUnauthorizedWhenCredentialsAreInvalid() throws Exception {
        // Arrange: simulamos que el microservicio de usuarios devolvió 401
        when(loginService.execute(ArgumentMatchers.any()))
                .thenThrow(new fis.auth.domain.error.InvalidCredentialsException("Credenciales inválidas"));

        LoginRequest request = new LoginRequest("juan@dominio.com", "Error123!");

        // Act & Assert: el handler global debe traducir la excepción a HTTP 401
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Auth: Credenciales inválidas")))
                .andExpect(jsonPath("$.code", is(401)));
    }

    @Test
    @DisplayName("login - debe responder 400 cuando el email está vacío")
    void loginShouldReturnBadRequestWhenEmailIsEmpty() throws Exception {
        // Arrange: propagamos un IllegalArgumentException como haría una validación previa
        when(loginService.execute(ArgumentMatchers.any()))
                .thenThrow(new IllegalArgumentException("El correo es obligatorio"));

        LoginRequest request = new LoginRequest("", "Seguro123!");

        // Act & Assert: el handler debe producir un 400 con el mensaje de negocio
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Auth: El correo es obligatorio")))
                .andExpect(jsonPath("$.code", is(400)));
    }

    @Test
    @DisplayName("login - debe responder 400 cuando la contraseña está vacía")
    void loginShouldReturnBadRequestWhenPasswordIsEmpty() throws Exception {
        // Arrange: validamos que también se maneje el mensaje para contraseña vacía
        when(loginService.execute(ArgumentMatchers.any()))
                .thenThrow(new IllegalArgumentException("La contraseña es obligatoria"));

        LoginRequest request = new LoginRequest("usuario@dominio.com", "");

        // Act & Assert: esperamos la traducción consistente a HTTP 400
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.message", is("Auth: La contraseña es obligatoria")))
                .andExpect(jsonPath("$.code", is(400)));
    }
}
