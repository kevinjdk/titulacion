package com.rce.titulacion.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rce.titulacion.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.Collections;

/**
 * Implementación personalizada de UserDetails para Spring Security.
 * 
 * Esta clase adapta la entidad User del sistema a la interfaz UserDetails
 * requerida por Spring Security. Proporciona la información necesaria
 * para la autenticación y autorización de usuarios.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    /**
     * Constructor para crear una instancia de UserDetailsImpl.
     * 
     * @param id ID único del usuario
     * @param username Nombre de usuario
     * @param password Contraseña encriptada del usuario
     */
    public UserDetailsImpl(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Método de fábrica para crear UserDetailsImpl desde una entidad User.
     * 
     * Convierte una entidad User del sistema en un objeto UserDetailsImpl
     * que puede ser utilizado por Spring Security para autenticación.
     * 
     * @param user Entidad User a convertir
     * @return UserDetailsImpl Nueva instancia creada a partir del usuario
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword());
    }

    /**
     * Obtiene las autoridades (roles) concedidas al usuario.
     * 
     * En la implementación actual, retorna una lista vacía ya que
     * no se han definido roles específicos en el sistema.
     * 
     * @return Collection<? extends GrantedAuthority> Lista vacía de autoridades
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * Obtiene la contraseña del usuario.
     * 
     * @return String Contraseña encriptada del usuario
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return String Nombre de usuario
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene el ID único del usuario.
     * 
     * @return Long ID del usuario
     */
    public Long getId() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}