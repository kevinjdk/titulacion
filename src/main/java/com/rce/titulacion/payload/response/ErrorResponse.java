package com.rce.titulacion.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Clase de respuesta para errores en la API.
 * Esta clase encapsula la información detallada de errores que ocurren durante
 * el procesamiento de peticiones HTTP, proporcionando información estructurada
 * que incluye timestamp, código de estado, mensaje de error y detalles adicionales.
 * 
 * <p>Utilizada principalmente por el {@link com.rce.titulacion.exception.GlobalExceptionHandler}
 * para proporcionar respuestas de error consistentes y detalladas a los clientes de la API.</p>
 * 
 * <p>Características principales:</p>
 * <ul>
 *   <li>Timestamp automático al momento del error</li>
 *   <li>Información del código de estado HTTP</li>
 *   <li>Mensaje descriptivo del error</li>
 *   <li>Ruta de la petición que causó el error</li>
 *   <li>Soporte para errores de validación de campos</li>
 * </ul>
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    /**
     * Timestamp del momento en que ocurrió el error.
     * Se establece automáticamente al crear la respuesta de error.
     */
    private LocalDateTime timestamp;
    
    /**
     * Código de estado HTTP numérico del error.
     * Ejemplo: 404, 400, 500, etc.
     */
    private int status;
    
    /**
     * Descripción textual del tipo de error HTTP.
     * Ejemplo: "Not Found", "Bad Request", "Internal Server Error".
     */
    private String error;
    
    /**
     * Mensaje descriptivo específico del error ocurrido.
     * Proporciona información detallada sobre la causa del error.
     */
    private String message;
    
    /**
     * Ruta de la petición HTTP que causó el error.
     * Útil para debugging y logging.
     */
    private String path;
    
    /**
     * Mapa de errores específicos de validación de campos.
     * La clave es el nombre del campo y el valor es el mensaje de error.
     * Se utiliza principalmente para errores de validación de formularios.
     */
    private Map<String, String> fieldErrors;

    /**
     * Constructor para crear una respuesta de error básica.
     * Establece automáticamente el timestamp al momento actual.
     * 
     * @param status El estado HTTP del error
     * @param message El mensaje descriptivo del error
     * @param path La ruta de la petición que causó el error
     */
    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    /**
     * Constructor para crear una respuesta de error con errores de validación de campos.
     * Establece automáticamente el timestamp al momento actual e incluye errores específicos de campos.
     * 
     * @param status El estado HTTP del error
     * @param message El mensaje descriptivo del error
     * @param path La ruta de la petición que causó el error
     * @param fieldErrors Mapa de errores de validación de campos (campo -> mensaje de error)
     */
    public ErrorResponse(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}