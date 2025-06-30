package com.rce.titulacion.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.rce.titulacion.security.services.UserDetailsImpl;

import java.security.Key;
import java.util.Date;

/**
 * Utilidad para la gestión de tokens JWT (JSON Web Tokens).
 * 
 * Esta clase proporciona métodos para generar, validar y extraer información
 * de tokens JWT utilizados para la autenticación en el sistema. Los tokens
 * son firmados usando HMAC SHA-256 y tienen un tiempo de expiración configurable.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${recetas.app.jwtSecret}")
    private String jwtSecret;

    @Value("${recetas.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT a partir de la información de autenticación.
     * 
     * Crea un token JWT que incluye el nombre de usuario como subject,
     * la fecha de emisión y la fecha de expiración. El token es firmado
     * usando la clave secreta configurada.
     * 
     * @param authentication Objeto de autenticación que contiene los detalles del usuario
     * @return String Token JWT generado
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera la clave de firma para los tokens JWT.
     * 
     * Convierte la clave secreta configurada (en base64) a una clave
     * HMAC SHA que puede ser utilizada para firmar y verificar tokens JWT.
     * 
     * @return Key Clave de firma HMAC SHA
     */
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     * 
     * Parsea el token JWT y extrae el subject (nombre de usuario) de sus claims.
     * El token debe ser válido y estar firmado con la clave correcta.
     * 
     * @param token Token JWT del cual extraer el nombre de usuario
     * @return String Nombre de usuario contenido en el token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Valida un token JWT.
     * 
     * Verifica que el token sea válido, esté correctamente firmado,
     * no haya expirado y tenga el formato correcto. Registra errores
     * específicos para diferentes tipos de problemas con el token.
     * 
     * @param authToken Token JWT a validar
     * @return boolean true si el token es válido, false en caso contrario
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}