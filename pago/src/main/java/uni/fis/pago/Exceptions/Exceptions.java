package uni.fis.pago.Exceptions;

public class Exceptions extends RuntimeException {
    private final String errorCode; 
    public Exceptions(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
