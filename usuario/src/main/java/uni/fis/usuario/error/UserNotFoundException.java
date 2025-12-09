package uni.fis.usuario.error;

public class UserNotFoundException extends UserDomainException {
  public UserNotFoundException(String message) {
    super(message);
  }

  public static UserNotFoundException byId(Integer id) {
    return new UserNotFoundException("Usuario no encontrado con ID: " + id);
  }

  public static UserNotFoundException byEmail(String email) {
    return new UserNotFoundException("Usuario no encontrado con email: " + email);
  }
}