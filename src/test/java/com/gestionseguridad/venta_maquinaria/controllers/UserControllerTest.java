package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetAllUsers() {
        User user1 = User.builder().name("Juan").build();
        User user2 = User.builder().name("Maria").build();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_Found() {
        Long id = 1L;
        User user = User.builder().id(id).name("Juan").build();
        when(userService.getUserById(id)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan", response.getBody().getName());
    }

    @Test
    void testGetUserById_NotFound() {
        Long id = 99L;
        when(userService.getUserById(id)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateUser() {
        User userToSave = User.builder().name("Nuevo").build();
        User userSaved = User.builder().id(1L).name("Nuevo").build();
        
        when(userService.saveUser(userToSave)).thenReturn(userSaved);

        User result = userController.createUser(userToSave);

        assertNotNull(result.getId());
        assertEquals("Nuevo", result.getName());
        verify(userService, times(1)).saveUser(userToSave);
    }

    @Test
    void testUpdateUser_Found() {
        Long id = 1L;
        User existingUser = User.builder().id(id).name("Viejo").build();
        User userUpdates = User.builder().name("Actualizado").build();
        
        when(userService.getUserById(id)).thenReturn(Optional.of(existingUser));
        when(userService.saveUser(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<User> response = userController.updateUser(userUpdates, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals("Actualizado", response.getBody().getName());
    }

    @Test
    void testUpdateUser_NotFound() {
        Long id = 99L;
        User userUpdates = User.builder().name("Fantasma").build();
        
        when(userService.getUserById(id)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.updateUser(userUpdates, id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, never()).saveUser(any());
    }
}