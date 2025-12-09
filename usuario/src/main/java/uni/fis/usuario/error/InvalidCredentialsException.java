package uni.fis.usuario.error;

public class InvalidCredentialsException extends UserDomainException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public static InvalidCredentialsException invalid() {
        return new InvalidCredentialsException("Credenciales inválidas");
    }
}