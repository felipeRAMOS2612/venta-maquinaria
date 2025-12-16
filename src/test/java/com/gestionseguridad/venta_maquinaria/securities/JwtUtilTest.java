package com.gestionseguridad.venta_maquinaria.securities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; 

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        
        String secretKey = "12345678901234567890123456789012"; 
        long expirationMs = 3600000; 

        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", secretKey);
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", expirationMs);
    }

    @Test
    void testGenerateToken() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();

        // When
        String token = jwtUtil.generateToken(userDetails);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();
        
        String token = jwtUtil.generateToken(userDetails);

        // When
        String username = jwtUtil.getUsernameFromToken(token);

        // Then
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_Success() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();
        
        String token = jwtUtil.generateToken(userDetails);

        // When
        boolean isValid = jwtUtil.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    void testValidateToken_Failure_WrongUser() {
        // Given
        when(userDetails.getUsername()).thenReturn("user1");
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();
        String token = jwtUtil.generateToken(userDetails);

        UserDetails otherUser = Mockito.mock(UserDetails.class);
        when(otherUser.getUsername()).thenReturn("user2");

        // When
        boolean isValid = jwtUtil.validateToken(token, otherUser);

        // Then
        assertFalse(isValid);
    }

    @Test
    void testValidateToken_Failure_MalformedToken() {
        // Este test cubre el bloque catch de validateToken
        String invalidToken = "token_falso_y_malformado";

        boolean isValid = jwtUtil.validateToken(invalidToken, userDetails);

        assertFalse(isValid);
    }
}