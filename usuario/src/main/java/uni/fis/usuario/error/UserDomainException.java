package uni.fis.usuario.error;

public abstract class UserDomainException extends RuntimeException {
  public UserDomainException(String message) {
    super(message);
  }
}