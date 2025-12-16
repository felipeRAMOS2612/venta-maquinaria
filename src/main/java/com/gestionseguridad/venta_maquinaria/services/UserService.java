package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(User user) {
        user.setPassword(this.passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    public void initializeTestUsers() {
        if (userRepository.count() == 0) {
            List<User> testUsers = List.of(
                User.builder()
                    .name("Administrador")
                    .email("admin@recetas.com")
                    .password("admin123")
                    .rol(User.Rol.DUENO)
                    .build(),
                User.builder()
                    .name("Juan Pérez")
                    .email("juan@recetas.com")
                    .password("user123")
                    .rol(User.Rol.AGRICULTOR)
                    .build(),
                User.builder()
                    .name("María García")
                    .email("maria@recetas.com")
                    .password("user123")
                    .rol(User.Rol.AGRICULTOR)
                    .build()
            );
            
            testUsers.forEach(this::saveUser);
        }
    }
}
