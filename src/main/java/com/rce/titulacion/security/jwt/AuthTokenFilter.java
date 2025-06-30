package com.rce.titulacion.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import com.rce.titulacion.security.services.UserDetailsServiceImpl;

import java.io.IOException;

/**
 * Filtro de autenticación JWT para procesar tokens en las peticiones HTTP.
 * 
 * Este filtro se ejecuta una vez por petición HTTP e intercepta las solicitudes
 * para extraer y validar tokens JWT del header Authorization. Si el token es
 * válido, establece la autenticación en el contexto de seguridad de Spring.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * Procesa cada petición HTTP para extraer y validar tokens JWT.
     * 
     * Este método extrae el token JWT del header Authorization, lo valida,
     * y si es válido, carga los detalles del usuario y establece la
     * autenticación en el contexto de seguridad de Spring.
     * 
     * @param request Petición HTTP entrante
     * @param response Respuesta HTTP
     * @param filterChain Cadena de filtros para continuar el procesamiento
     * @throws ServletException Si ocurre un error durante el filtrado
     * @throws IOException Si ocurre un error de entrada/salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization de la petición HTTP.
     * 
     * Busca el header "Authorization" y extrae el token JWT removiendo
     * el prefijo "Bearer ". Si el header no existe o no tiene el formato
     * correcto, retorna null.
     * 
     * @param request Petición HTTP de la cual extraer el token
     * @return String Token JWT sin el prefijo "Bearer ", o null si no se encuentra
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}