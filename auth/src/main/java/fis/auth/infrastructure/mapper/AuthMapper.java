package fis.auth.infrastructure.mapper;

import fis.auth.domain.model.Login;
import fis.auth.domain.model.SignIn;
import fis.auth.infrastructure.dto.request.DocumentoRequest;
import fis.auth.infrastructure.dto.request.LoginRequest;
import fis.auth.infrastructure.dto.request.SignInRequest;

public class AuthMapper {

    public static Login toDomain(LoginRequest loginRequest) {
        return new Login(loginRequest.email(), loginRequest.password());
    }

    public static SignIn toDomain(SignInRequest signInRequest) {
        return new SignIn(
                signInRequest.name(),
                signInRequest.apellido1(),
                signInRequest.apellido2(),
                signInRequest.fechaNacimiento(),
                DocumentoMapper.toDomain(signInRequest.documento()),
                signInRequest.imgPerfil(), // Si puede ser null
                signInRequest.email(),
                signInRequest.strikes() != null ? signInRequest.strikes() : 0, // Default 0
                signInRequest.idRol(),
                signInRequest.password(),
                signInRequest.emailTutor()
        );
    }

    public static SignInRequest toRequest(SignIn signIn) {

        DocumentoRequest documentoRequest = null;

        if (signIn.documento() != null) {
            documentoRequest = new DocumentoRequest(
                    signIn.documento().id(),
                    signIn.documento().tipoDocumento() != null ? signIn.documento().tipoDocumento().id() : 0,
                    signIn.documento().numeroDocumento(),
                    signIn.documento().fechaExpiracion()
            );
        }

        return new SignInRequest(
                signIn.name(),
                signIn.apellido1(),
                signIn.apellido2(),
                signIn.fechaNacimiento(),
                documentoRequest,
                signIn.imgPerfil(),
                signIn.email(),
                signIn.strikes(),
                signIn.idRol(),
                signIn.password(),
                signIn.emailTutor()
        );
    }

}
