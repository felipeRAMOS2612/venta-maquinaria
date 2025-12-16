package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.repositories.MachineryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachineryServiceTest {

    @Mock
    private MachineryRepository machineryRepository;

    @InjectMocks
    private MachineryService machineryService;

    @Test
    void testInitializeMachinery_WhenEmpty() {
        // Caso: Base de datos vacía, debe insertar datos iniciales
        when(machineryRepository.count()).thenReturn(0L);
        
        machineryService.initializeMachinery();

        // Verifica que se llamó a saveAll con una lista
        verify(machineryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testInitializeMachinery_WhenNotEmpty() {
        // Caso: Ya hay datos, no debe hacer nada
        when(machineryRepository.count()).thenReturn(10L);
        
        machineryService.initializeMachinery();

        verify(machineryRepository, never()).saveAll(anyList());
    }

    @Test
    void testSaveMachinery() {
        Machinery maquina = new Machinery();
        when(machineryRepository.save(any(Machinery.class))).thenReturn(maquina);

        Machinery result = machineryService.saveMachinery(maquina);

        assertNotNull(result);
        verify(machineryRepository).save(maquina);
    }

    @Test
    void testGetMachineryById() {
        Long id = 1L;
        Machinery maquina = new Machinery();
        when(machineryRepository.findById(id)).thenReturn(Optional.of(maquina));

        Optional<Machinery> result = machineryService.getMachineryById(id);

        assertTrue(result.isPresent());
    }

    @Test
    void testGetAllMachinery() {
        when(machineryRepository.findAll()).thenReturn(Arrays.asList(new Machinery(), new Machinery()));

        List<Machinery> result = machineryService.getAllMachinery();

        assertEquals(2, result.size());
    }

    // --- Tests para los filtros con Streams ---

    @Test
    void testGetPopularMachinery() {
        // Crea maquinas con distinta popularidad
        Machinery m1 = Machinery.builder().name("Baja").popularity(10).build();
        Machinery m2 = Machinery.builder().name("Alta").popularity(100).build();
        Machinery m3 = Machinery.builder().name("Media").popularity(50).build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2, m3));

        List<Machinery> result = machineryService.getPopularMachinery();

        // Debe ordenar de mayor a menor: 100, 50, 10
        assertEquals(3, result.size());
        assertEquals("Alta", result.get(0).getName());
        assertEquals("Media", result.get(1).getName());
        assertEquals("Baja", result.get(2).getName());
    }

    @Test
    void testSearchMachineryByName() {
        Machinery m1 = Machinery.builder().name("Tractor John Deere").build();
        Machinery m2 = Machinery.builder().name("Excavadora CAT").build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = machineryService.searchMachineryByName("tractor");

        assertEquals(1, result.size());
        assertEquals("Tractor John Deere", result.get(0).getName());
    }

    @Test
    void testGetMachineryByType() {
        Machinery m1 = Machinery.builder().type(Machinery.MachineryType.TRACTOR).build();
        Machinery m2 = Machinery.builder().type(Machinery.MachineryType.COSECHADORA).build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = machineryService.getMachineryByType(Machinery.MachineryType.TRACTOR);

        assertEquals(1, result.size());
        assertEquals(Machinery.MachineryType.TRACTOR, result.get(0).getType());
    }

    @Test
    void testGetMachineryByLocation() {
        Machinery m1 = Machinery.builder().location("Santiago").build();
        Machinery m2 = Machinery.builder().location("Valparaíso").build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = machineryService.getMachineryByLocation("Santiago");

        assertEquals(1, result.size());
        assertEquals("Santiago", result.get(0).getLocation());
    }

    @Test
    void testGetMachineryByBrand() {
        Machinery m1 = Machinery.builder().brand("CAT").build();
        Machinery m2 = Machinery.builder().brand("John Deere").build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = machineryService.getMachineryByBrand("CAT");

        assertEquals(1, result.size());
        assertEquals("CAT", result.get(0).getBrand());
    }

    @Test
    void testGetAvailableMachinery() {
        Machinery m1 = Machinery.builder().available(true).build();
        Machinery m2 = Machinery.builder().available(false).build();

        when(machineryRepository.findAll()).thenReturn(Arrays.asList(m1, m2));

        List<Machinery> result = machineryService.getAvailableMachinery();

        assertEquals(1, result.size());
        assertTrue(result.get(0).isAvailable());
    }

    @Test
    void testGetRecentMachinery() {
        when(machineryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Machinery> result = machineryService.getRecentMachinery();
        
        assertNotNull(result);
    }
}