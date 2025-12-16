package com.gestionseguridad.venta_maquinaria.config;

import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void testRun() throws Exception {
        // When
        dataInitializer.run();

        // Then
        verify(userService, times(1)).initializeTestUsers();
    }
}