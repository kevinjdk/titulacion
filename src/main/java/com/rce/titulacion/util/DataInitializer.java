package com.rce.titulacion.util;

import com.rce.titulacion.model.User;
import com.rce.titulacion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@recetas.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            userRepository.save(admin);
            System.out.println("Admin user created with username 'admin', email 'admin@recetas.com' and password 'adminpass'");
        }
    }
}