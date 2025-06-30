package com.rce.titulacion.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase de respuesta genérica para la API REST.
 * 
 * Esta clase proporciona una estructura estandarizada para todas las respuestas
 * de la API, incluyendo tanto respuestas exitosas como de error. Utiliza generics
 * para permitir diferentes tipos de datos en la respuesta.
 * 
 * @param <T> El tipo de datos que contendrá la respuesta
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    /**
     * Indica si la operación fue exitosa o no.
     * true para operaciones exitosas, false para errores.
     */
    private boolean success;
    
    /**
     * Mensaje descriptivo de la respuesta.
     * Proporciona información adicional sobre el resultado de la operación.
     */
    private String message;
    
    /**
     * Datos de la respuesta.
     * Contiene la información solicitada en caso de operaciones exitosas.
     * Puede ser de cualquier tipo especificado por el parámetro genérico T.
     */
    private T data;
    
    /**
     * Información detallada del error en caso de fallo.
     * Solo se incluye cuando success es false.
     */
    private ErrorResponse error;

    /**
     * Constructor para crear una respuesta con datos específicos.
     * 
     * @param success Indica si la operación fue exitosa
     * @param message Mensaje descriptivo de la respuesta
     * @param data Datos a incluir en la respuesta
     */
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = null;
    }

    /**
     * Método factory para crear una respuesta exitosa con datos y mensaje personalizado.
     * 
     * @param <T> Tipo de datos de la respuesta
     * @param message Mensaje descriptivo del éxito
     * @param data Datos a incluir en la respuesta
     * @return ApiResponse configurada como exitosa con los datos proporcionados
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /**
     * Método factory para crear una respuesta exitosa con datos y mensaje por defecto.
     * 
     * @param <T> Tipo de datos de la respuesta
     * @param data Datos a incluir en la respuesta
     * @return ApiResponse configurada como exitosa con mensaje por defecto
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Operación exitosa", data);
    }

    /**
     * Método factory para crear una respuesta exitosa solo con mensaje, sin datos.
     * 
     * @param message Mensaje descriptivo del éxito
     * @return ApiResponse configurada como exitosa sin datos
     */
    public static ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /**
     * Método factory para crear una respuesta de error con detalles específicos.
     * 
     * @param message Mensaje descriptivo del error
     * @param error Detalles completos del error
     * @return ApiResponse configurada como error con los detalles proporcionados
     */
    public static ApiResponse<Void> error(String message, ErrorResponse error) {
        return new ApiResponse<>(false, message, null, error);
    }
}