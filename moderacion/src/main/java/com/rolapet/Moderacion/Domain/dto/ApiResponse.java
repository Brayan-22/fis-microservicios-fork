package com.rolapet.Moderacion.Domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase genérica para estandarizar las respuestas de la API.
 * Permite retornar mensajes consistentes en formato JSON junto con
 * un estado, código HTTP y datos opcionales.
 *
 * @param <T> Tipo de dato que se devolverá en la respuesta.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    /** Estado de la respuesta: success, error, informacion, etc. */
    private String status;

    /** Mensaje descriptivo sobre el resultado de la operación. */
    private String message;

    /** Datos devueltos por la operación (puede ser null). */
    private T data;

    /** Código asociado a la operación (normalmente HTTP). */
    private Integer code;

    /**
     * Respuesta exitosa con mensaje y datos.
     *
     * @param message Mensaje de éxito.
     * @param data    Datos retornados.
     * @return ApiResponse con estado "success".
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .data(data)
                .code(200)
                .build();
    }

    /**
     * Respuesta exitosa sin datos, solo mensaje.
     *
     * @param message Mensaje de éxito.
     * @return ApiResponse con estado "success".
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .status("success")
                .message(message)
                .code(200)
                .build();
    }

    /**
     * Respuesta de error con mensaje y código asociado.
     *
     * @param message Mensaje de error.
     * @param code    Código del error (generalmente error HTTP).
     * @return ApiResponse con estado "error".
     */
    public static <T> ApiResponse<T> error(String message, Integer code) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .code(code)
                .build();
    }

    /**
     * Respuesta informativa con mensaje y código personalizado.
     *
     * @param message Mensaje informativo.
     * @param code    Código asociado.
     * @return ApiResponse con estado "informacion".
     */
    public static <T> ApiResponse<T> info(String message, Integer code) {
        return ApiResponse.<T>builder()
                .status("informacion")
                .message(message)
                .code(code)
                .build();
    }
}

