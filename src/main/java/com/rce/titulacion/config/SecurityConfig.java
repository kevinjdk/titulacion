package com.rce.titulacion.config;

import com.rce.titulacion.security.jwt.AuthEntryPointJwt;
import com.rce.titulacion.security.jwt.AuthTokenFilter;
import com.rce.titulacion.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de Spring Security.
 * 
 * Esta clase configura la seguridad de la aplicación, incluyendo la autenticación JWT,
 * autorización de endpoints, configuración de CORS, y la cadena de filtros de seguridad.
 * Define qué endpoints son públicos y cuáles requieren autenticación.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Configura el filtro de autenticación JWT.
     * 
     * @return AuthTokenFilter Filtro personalizado para procesar tokens JWT
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Configura el proveedor de autenticación DAO.
     * 
     * Establece el servicio de detalles de usuario y el codificador de contraseñas
     * para la autenticación basada en base de datos.
     * 
     * @return DaoAuthenticationProvider Proveedor de autenticación configurado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configura el administrador de autenticación.
     * 
     * @param authConfig Configuración de autenticación de Spring
     * @return AuthenticationManager Administrador de autenticación
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Configura el codificador de contraseñas.
     * 
     * Utiliza BCrypt para el hash seguro de contraseñas.
     * 
     * @return PasswordEncoder Codificador BCrypt para contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * 
     * Define la configuración principal de seguridad incluyendo:
     * - Deshabilitación de CSRF
     * - Configuración de manejo de excepciones
     * - Política de sesiones stateless
     * - Autorización de endpoints (públicos vs protegidos)
     * - Integración del filtro JWT
     * 
     * @param http Objeto HttpSecurity para configurar
     * @return SecurityFilterChain Cadena de filtros configurada
     * @throws Exception Si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/platos/**").permitAll()
                        .requestMatchers("/api/categorias/**").permitAll()
                        .requestMatchers("/api/regiones/**").permitAll()
                        .requestMatchers("/api/provincias/**").permitAll()
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}