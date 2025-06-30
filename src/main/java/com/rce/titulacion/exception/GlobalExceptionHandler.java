package com.rce.titulacion.exception;

import com.rce.titulacion.payload.response.ApiResponse;
import com.rce.titulacion.payload.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // Indica que esta clase maneja excepciones globalmente
public class GlobalExceptionHandler {

    // Maneja la excepción personalizada ResourceNotFoundException (HTTP 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(status, ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(ApiResponse.error("Recurso no encontrado", errorResponse), status);
    }

    // Maneja errores de validación (@Valid) (HTTP 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        // Recopila todos los errores de campo y sus mensajes
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse errorResponse = new ErrorResponse(status, "Falló la validación de los datos", request.getRequestURI(), errors);
        return new ResponseEntity<>(ApiResponse.error("Error de validación", errorResponse), status);
    }

    // Maneja errores de credenciales inválidas (ej. en AuthController) (HTTP 401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = new ErrorResponse(status, "Credenciales inválidas: usuario o contraseña incorrectos.", request.getRequestURI());
        return new ResponseEntity<>(ApiResponse.error("Fallo de autenticación", errorResponse), status);
    }

    // Maneja errores de acceso denegado (@PreAuthorize) (HTTP 403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        ErrorResponse errorResponse = new ErrorResponse(status, "Acceso denegado: No tienes permiso para acceder a este recurso.", request.getRequestURI());
        return new ResponseEntity<>(ApiResponse.error("Fallo de autorización", errorResponse), status);
    }

    // Maneja cualquier otra excepción no capturada (HTTP 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = new ErrorResponse(status, "Ocurrió un error inesperado: " + ex.getMessage(), request.getRequestURI());
        return new ResponseEntity<>(ApiResponse.error("Error interno del servidor", errorResponse), status);
    }
}