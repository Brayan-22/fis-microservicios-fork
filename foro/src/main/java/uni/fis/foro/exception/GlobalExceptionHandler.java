package uni.fis.foro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uni.fis.foro.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    
    
    @ExceptionHandler(CategoriaNoEncontradaException.class)
    public ResponseEntity<ApiResponse<Object>> handleCategoriaNoEncontrada(CategoriaNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), 404));
    }

    @ExceptionHandler(ForoNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleForoNoEncontrado(ForoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ex.getMessage(), 404));
    }

    @ExceptionHandler(ForoCategoriaNoValidaException.class)
    public ResponseEntity<ApiResponse<Object>> handleForoCategoriaNoValida(ForoCategoriaNoValidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), 400));
    }

    // Excepción general
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(ex.getMessage(), 500)
        );
    }
}
