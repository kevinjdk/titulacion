package com.rce.titulacion.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada para manejar errores de autenticación no autorizada.
 * 
 * Esta clase implementa AuthenticationEntryPoint y se encarga de manejar
 * las situaciones donde un usuario intenta acceder a un recurso protegido
 * sin estar debidamente autenticado. Registra el error y envía una respuesta
 * HTTP 401 (Unauthorized) al cliente.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    /**
     * Maneja los errores de autenticación no autorizada.
     * 
     * Este método es llamado por Spring Security cuando un usuario intenta
     * acceder a un recurso protegido sin las credenciales adecuadas.
     * Registra el error para propósitos de auditoría y debugging, y envía
     * una respuesta HTTP 401 al cliente.
     * 
     * @param request Petición HTTP que causó el error de autenticación
     * @param response Respuesta HTTP para enviar el error al cliente
     * @param authException Excepción que contiene los detalles del error de autenticación
     * @throws IOException Si ocurre un error de entrada/salida
     * @throws ServletException Si ocurre un error del servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
    }
}