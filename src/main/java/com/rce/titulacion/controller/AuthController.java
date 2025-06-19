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

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600) // Permite solicitudes desde cualquier origen para Angular
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Autentica al usuario usando el AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Establece la autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Genera el token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Obtiene los detalles del usuario autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Devuelve el token JWT y los datos del usuario en la respuesta
        return ResponseEntity.ok(new JwtResponse(jwt,
                                                 userDetails.getId(),
                                                 userDetails.getUsername()));
    }
}