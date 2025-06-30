package com.rce.titulacion.payload.response;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para respuestas de autenticación JWT.
 * 
 * Esta clase representa la respuesta que se envía al cliente después
 * de una autenticación exitosa, conteniendo el token JWT y la información
 * básica del usuario autenticado.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Data
public class JwtResponse {
    /** Token JWT para autenticación en futuras peticiones */
    private String token;
    
    /** Tipo de token, siempre "Bearer" para JWT */
    private String type = "Bearer";
    
    /** ID único del usuario autenticado */
    private Long id;
    
    /** Nombre de usuario del usuario autenticado */
    private String username;

    /**
     * Constructor para crear una respuesta JWT.
     * 
     * @param accessToken Token JWT generado para el usuario
     * @param id ID único del usuario autenticado
     * @param username Nombre de usuario del usuario autenticado
     */
    public JwtResponse(String accessToken, Long id, String username) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
    }
}