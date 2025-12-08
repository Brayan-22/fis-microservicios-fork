package uni.fis.catalogo.Exceptions;

public class ItemNotFoundException extends RuntimeException {
    private final String errorCode; 
    public ItemNotFoundException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }  
}
