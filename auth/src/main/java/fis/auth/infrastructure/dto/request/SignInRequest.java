package fis.auth.infrastructure.dto.request;

public record SignInRequest(
                String name, // necesario
                String apellido1, // necesario
                String apellido2, // no necesario
                String fechaNacimiento, // necesario
                DocumentoRequest documento, // necesario y unico
                Integer imgPerfil, // no necesario
                String email, // necesario y unico
                Integer strikes, // por defecto 0 al crear
                Integer idRol, // no necesario
                String password,
                String emailTutor// necesario
) {
}