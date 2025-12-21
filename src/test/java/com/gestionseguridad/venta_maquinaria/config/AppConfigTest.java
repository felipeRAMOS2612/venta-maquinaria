package com.gestionseguridad.venta_maquinaria.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void testPasswordEncoderBeanCreation() {
        AppConfig appConfig = new AppConfig();

        PasswordEncoder encoder = appConfig.passwordEncoder();

        assertNotNull(encoder, "El PasswordEncoder no debería ser nulo");
        
        assertTrue(encoder instanceof BCryptPasswordEncoder, "El bean debe ser una instancia de BCryptPasswordEncoder");

        String rawPassword = "password123";
        String encodedPassword = encoder.encode(rawPassword);

        assertNotEquals(rawPassword, encodedPassword, "La contraseña debe estar encriptada");
        assertTrue(encoder.matches(rawPassword, encodedPassword), "El encoder debe poder validar la contraseña");
    }
}