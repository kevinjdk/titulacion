package com.rce.titulacion.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para solicitudes de inicio de sesión.
 * 
 * Esta clase representa los datos requeridos para que un usuario
 * inicie sesión en el sistema. Incluye validaciones para asegurar
 * que los campos obligatorios no estén vacíos.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Data
public class LoginRequest {
    /**
     * Nombre de usuario para autenticación.
     * No puede estar vacío o contener solo espacios en blanco.
     */
    @NotBlank
    private String username;

    /**
     * Contraseña del usuario para autenticación.
     * No puede estar vacía o contener solo espacios en blanco.
     */
    @NotBlank
    private String password;
}