package uni.fis.usuario.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para estructurar las respuestas de la API.
 * Permite un formato uniforme tanto para éxito como para error.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private Integer code;

    // -----------------------------
    // Métodos estáticos de fábrica
    // -----------------------------

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .code(200)
                .build();
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .code(200)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, Integer code) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .build();
    }
}