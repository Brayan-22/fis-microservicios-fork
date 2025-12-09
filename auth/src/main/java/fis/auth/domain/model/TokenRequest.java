package fis.auth.domain.model;

public record TokenRequest(
        Integer userId,
        Integer idRol,
        String rol, // este va a ser el Rol
        String email) {
}
