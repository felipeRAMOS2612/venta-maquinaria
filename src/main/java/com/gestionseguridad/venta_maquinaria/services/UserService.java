package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    public void initializeTestUsers() {
        if (userRepository.count() == 0) {
            List<User> testUsers = List.of(
                User.builder()
                    .name("Administrador")
                    .email("dueno@agrorent.com")
                    .password("admin123")
                    .rol(User.Rol.DUENO)
                    .build(),
                User.builder()
                    .name("Juan Perez")
                    .email("juan@agricultor.com")
                    .password("user123")
                    .rol(User.Rol.AGRICULTOR)
                    .build(),
                User.builder()
                    .name("Maria Garcia")
                    .email("maria@agricultor.com")
                    .password("user123")
                    .rol(User.Rol.AGRICULTOR)
                    .build()
            );
            
            testUsers.forEach(this::saveUser);
        }
    }
}