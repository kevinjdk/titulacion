package com.rce.titulacion.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error; // Ej: "Not Found", "Bad Request"
    private String message; // Mensaje descriptivo del error
    private String path; // La URL de la solicitud que causó el error
    private Map<String, String> fieldErrors; // Para errores de validación de campos específicos

    // Constructor para errores generales
    public ErrorResponse(HttpStatus status, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
    }

    // Constructor para errores de validación
    public ErrorResponse(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {
        this.timestamp = LocalDateTime.now();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }
}