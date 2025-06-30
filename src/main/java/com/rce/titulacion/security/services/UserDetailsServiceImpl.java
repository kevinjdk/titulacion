package com.rce.titulacion.security.services;

import com.rce.titulacion.model.User;
import com.rce.titulacion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de detalles de usuario para Spring Security.
 * 
 * Esta clase implementa la interfaz UserDetailsService de Spring Security
 * y proporciona la funcionalidad para cargar los detalles de un usuario
 * desde la base de datos durante el proceso de autenticación.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    /**
     * Carga los detalles de un usuario por su nombre de usuario.
     * 
     * Este método es llamado por Spring Security durante el proceso de
     * autenticación. Busca al usuario en la base de datos y convierte
     * la entidad User en un objeto UserDetailsImpl que implementa UserDetails.
     * 
     * @param username Nombre de usuario del usuario a cargar
     * @return UserDetails Objeto con los detalles del usuario para autenticación
     * @throws UsernameNotFoundException Si no se encuentra un usuario con el nombre especificado
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}