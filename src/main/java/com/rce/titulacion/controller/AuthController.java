package com.rce.titulacion.controller;

import com.rce.titulacion.security.jwt.JwtUtils;
import com.rce.titulacion.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.rce.titulacion.payload.request.LoginRequest;
import com.rce.titulacion.payload.response.JwtResponse;

/**
 * Controlador REST para la gestión de autenticación de usuarios.
 * 
 * Este controlador maneja los endpoints relacionados con la autenticación
 * de usuarios en el sistema, incluyendo el inicio de sesión y la generación
 * de tokens JWT para el acceso a endpoints protegidos.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * Este endpoint público permite a los usuarios iniciar sesión en el sistema
     * proporcionando sus credenciales (usuario y contraseña). Si las credenciales
     * son válidas, se genera y devuelve un token JWT que puede ser utilizado
     * para acceder a endpoints protegidos.
     * 
     * @param loginRequest Objeto que contiene las credenciales del usuario (username y password)
     * @return ResponseEntity conteniendo JwtResponse con el token JWT y datos del usuario
     * @throws org.springframework.security.authentication.BadCredentialsException si las credenciales son inválidas
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getUsername()));
    }
}