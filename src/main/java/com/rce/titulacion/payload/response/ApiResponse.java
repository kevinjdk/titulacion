package com.rce.titulacion.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Solo incluye campos no nulos en la respuesta JSON
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private ErrorResponse error; // Para detalles del error si 'success' es falso

    // Constructor para respuestas de éxito
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = null; // No hay error en una respuesta exitosa
    }

    // Métodos de fábrica estáticos para respuestas de éxito
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operación exitosa", data);
    }

    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    // Método de fábrica estático para errores (usado por GlobalExceptionHandler)
    public static ApiResponse<Void> error(String message, ErrorResponse error) {
        // Llamamos al constructor de 4 argumentos (de @AllArgsConstructor)
        // Pasamos 'null' para el campo 'data' y el objeto 'error' para el campo 'error'.
        return new ApiResponse<>(false, message, null, error);
    }
}