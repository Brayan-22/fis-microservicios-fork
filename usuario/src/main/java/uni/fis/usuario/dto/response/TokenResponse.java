package uni.fis.usuario.dto.response;

public record TokenResponse(
        Integer userId,
        Integer idRol,
        String rol, // este va a ser el Rol
        String email
) {
}
