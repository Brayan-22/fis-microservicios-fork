package uni.fis.usuario.dto;

public record UserDto(
        Integer id,
        String nombre,
        String apellido_1,
        String apellido_2,
        String email,
        Integer idRol,
        String rolName
) {
}
