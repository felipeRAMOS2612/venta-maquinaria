package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.dto.AuthRequest;
import com.gestionseguridad.venta_maquinaria.dto.AuthResponse;
import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.securities.JwtUtil;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRestControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthRestController authRestController;

    @Test
    void testLogin() {
        // Given
        AuthRequest request = new AuthRequest();
        
        // Mocks de seguridad profunda
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("fake-jwt-token");

        // When
        ResponseEntity<AuthResponse> response = authRestController.login(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Verifica que el token que devuelve es el que generó nuestro mock
        assertEquals("fake-jwt-token", response.getBody().getToken()); 
    }

    @Test
    void testRegister() {
        // Given
        User userInput = new User();
        User userSaved = new User();
        
        when(userService.saveUser(any(User.class))).thenReturn(userSaved);

        // When
        ResponseEntity<User> response = authRestController.register(userInput);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService, times(1)).saveUser(userInput);
    }
}