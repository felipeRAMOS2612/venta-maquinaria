package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private MachineryService machineryService;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    void testIndex() {
        // Given
        // Simula que el servicio devuelve listas vacías (no importa el contenido, solo que responda)
        when(machineryService.getRecentMachinery()).thenReturn(Collections.emptyList());
        when(machineryService.getPopularMachinery()).thenReturn(Collections.emptyList());

        // When
        String viewName = homeController.index(model);

        // Then
        //Verifica que devuelve la vista "index"
        assertEquals("index", viewName);

        //Verifica que llamó a los servicios
        verify(machineryService, times(1)).getRecentMachinery();
        verify(machineryService, times(1)).getPopularMachinery();

        //Verifica que agregó los atributos al modelo
        verify(model).addAttribute(eq("recentMachineries"), any());
        verify(model).addAttribute(eq("popularMachineries"), any());
    }
}