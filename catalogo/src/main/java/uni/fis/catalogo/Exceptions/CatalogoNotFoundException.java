package uni.fis.catalogo.Exceptions;

public class CatalogoNotFoundException extends RuntimeException {
    private final String errorCode; 
    public CatalogoNotFoundException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
