package uni.fis.usuario.error;

public class UserAlreadyExistsException extends UserDomainException {
    private UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException byEmail(String email) {
        return new UserAlreadyExistsException("Ya existe un usuario con el email: " + email);
    }

    public static UserAlreadyExistsException byDocument(String document) {
        return new UserAlreadyExistsException("Ya existe un usuario con el documento: " + document);
    }
}