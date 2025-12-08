package uni.fis.contenido.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uni.fis.contenido.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PublicacionNoEncontradaException.class)
    public ResponseEntity<ApiResponse<Object>> handlePublicacionNoEncontrada(PublicacionNoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(ex.getMessage(), 404)
        );
    }

    @ExceptionHandler(LikeDuplicadoException.class)
    public ResponseEntity<ApiResponse<Object>> handleLikeDuplicado(LikeDuplicadoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error(ex.getMessage(), 400)
        );
    }

    @ExceptionHandler(ComentarioNoEncontradoException.class)
    public ResponseEntity<ApiResponse<Object>> handleComentarioNoEncontrado(ComentarioNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ApiResponse.error(ex.getMessage(), 404)
        );
    }

    @ExceptionHandler(ContenidoNoValidoException.class)
    public ResponseEntity<ApiResponse<Object>> handleContenidoNoValido(ContenidoNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error(ex.getMessage(), 400)
        );
    }

    @ExceptionHandler(PuntoInteresNoValidoException.class)
    public ResponseEntity<ApiResponse<Object>> handlePuntoInteresNoValido(PuntoInteresNoValidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ApiResponse.error(ex.getMessage(), 400)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ApiResponse.error(ex.getMessage(), 500)
        );
    }
}
