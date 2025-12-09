package fis.auth.domain.model;

public record SignIn(
        String name,
        String apellido1,
        String apellido2,
        String fechaNacimiento,
        Documento documento,
        Integer imgPerfil,
        String email,
        Integer strikes,
        Integer idRol,
        String password,
        String emailTutor
) {

    public SignIn withPassword(String newPassword) {
        return new SignIn(
                this.name,
                this.apellido1,
                this.apellido2,
                this.fechaNacimiento,
                this.documento,
                this.imgPerfil,
                this.email,
                this.strikes,
                this.idRol,
                newPassword,
                null // este es completamente normal que sea nuelo
        );
    }
}
