package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder; 
    @InjectMocks
    private UserService userService;

    @Test
    void testSaveUser() {
        User usuarioNuevo = User.builder()
                .name("Test User")
                .email("test@email.com")
                .password("clavePlana")
                .rol(User.Rol.AGRICULTOR)
                .build();

        when(passwordEncoder.encode("clavePlana")).thenReturn("claveEncriptada");
        
        when(userRepository.save(any(User.class))).thenReturn(usuarioNuevo);

        User resultado = userService.saveUser(usuarioNuevo);

        assertNotNull(resultado);
        verify(passwordEncoder, times(1)).encode("clavePlana"); 
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById() {
        User usuario = User.builder().id(1L).name("Juan").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<User> resultado = userService.getUserById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan", resultado.get().getName());
    }

    @Test
    void testGetAllUsers() {
        List<User> lista = Arrays.asList(
                User.builder().name("U1").build(),
                User.builder().name("U2").build()
        );
        when(userRepository.findAll()).thenReturn(lista);

        List<User> resultado = userService.getAllUsers();

        assertEquals(2, resultado.size());
    }

    @Test
    void testGetUserByEmail() {
        User usuario = User.builder().email("admin@test.com").build();
        when(userRepository.findByEmail("admin@test.com")).thenReturn(usuario);

        User resultado = userService.getUserByEmail("admin@test.com");

        assertNotNull(resultado);
        assertEquals("admin@test.com", resultado.getEmail());
    }

    @Test
    void testInitializeTestUsers_WhenDbIsEmpty() {
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("claveEncriptada");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        userService.initializeTestUsers();

        verify(userRepository, times(3)).save(any(User.class));
    }

    @Test
    void testInitializeTestUsers_WhenDbIsNotEmpty() {
        when(userRepository.count()).thenReturn(5L);

        userService.initializeTestUsers();

        verify(userRepository, times(0)).save(any(User.class));
    }
}