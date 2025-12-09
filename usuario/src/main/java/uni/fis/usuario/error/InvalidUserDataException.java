package uni.fis.usuario.error;

public class InvalidUserDataException extends UserDomainException {
  public InvalidUserDataException(String message) {
    super(message);
  }

  public static InvalidUserDataException missingField(String field) {
    return new InvalidUserDataException("Campo requerido faltante: " + field);
  }

  public static InvalidUserDataException invalidField(String field, String value) {
    return new InvalidUserDataException("Valor inválido para " + field + ": " + value);
  }
}