package uni.fis.usuario.dto.request;

public record UserValidateRequest(
        String email,
        String password
) {
}
