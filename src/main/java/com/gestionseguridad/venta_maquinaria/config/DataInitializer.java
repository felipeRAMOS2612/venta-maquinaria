package com.gestionseguridad.venta_maquinaria.config;

import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.initializeTestUsers();
        // La maquinaria se inicializa automáticamente con @PostConstruct en MachineryService
    }
}