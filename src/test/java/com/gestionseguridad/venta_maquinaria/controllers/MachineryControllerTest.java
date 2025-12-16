package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MachineryControllerTest {

    @Mock
    private MachineryService machineryService;

    @Mock
    private Model model;

    @InjectMocks
    private MachineryController machineryController;

    // --- Test para la página de búsqueda inicial ---
    @Test
    void testSearchPage() {
        when(machineryService.getAllMachinery()).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchPage(model);

        assertEquals("search", viewName);
        verify(model).addAttribute(eq("machineries"), any());
        verify(model).addAttribute("searchPerformed", false);
    }

    @Test
    void testSearchResults_ByName() {
        // Caso: Se busca por Nombre
        String name = "Tractor";
        when(machineryService.searchMachineryByName(name)).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchResults(name, null, null, null, null, model);

        assertEquals("search", viewName);
        verify(machineryService).searchMachineryByName(name);
        verify(model).addAttribute("searchPerformed", true);
    }

    @Test
    void testSearchResults_ByType_WhenNameIsEmpty() {
        // Caso: El nombre está vacio (""), debe saltar al Type
        // Esto cubre la parte "!name.isEmpty()" del if
        String name = ""; 
        String type = "TRACTOR";
        
        when(machineryService.getMachineryByType(any())).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchResults(name, type, null, null, null, model);

        assertEquals("search", viewName);
        verify(machineryService).getMachineryByType(any());
        // Verifica que no buscó por nombre aunque el string no era null
        verify(machineryService, never()).searchMachineryByName(anyString());
    }

    @Test
    void testSearchResults_ByBrand_WhenNameAndTypeAreEmpty() {
        // Caso: Nombre y Tipo vacios (""), debe saltar a Brand
        String name = "";
        String type = "";
        String brand = "CAT";
        
        when(machineryService.getMachineryByBrand(brand)).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchResults(name, type, brand, null, null, model);

        assertEquals("search", viewName);
        verify(machineryService).getMachineryByBrand(brand);
    }

    @Test
    void testSearchResults_ByLocation_WhenOthersAreEmpty() {
        // Caso: Nombre, Tipo y Marca vacios (""), debe saltar a Location
        String location = "Santiago";
        
        when(machineryService.getMachineryByLocation(location)).thenReturn(Collections.emptyList());

        // Pasa cadenas vacías en vez de nulls para forzar la lógica completa
        String viewName = machineryController.searchResults("", "", "", location, null, model);

        assertEquals("search", viewName);
        verify(machineryService).getMachineryByLocation(location);
    }

    @Test
    void testSearchResults_NoFilters_AllNull() {
        when(machineryService.getAllMachinery()).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchResults(null, null, null, null, null, model);

        assertEquals("search", viewName);
        verify(machineryService).getAllMachinery();
    }
    
    @Test
    void testSearchResults_NoFilters_AllEmptyStrings() {
        // Caso: Todas son cadenas vacías (fuerza a pasar por todos los ifs y caer al else final)
        when(machineryService.getAllMachinery()).thenReturn(Collections.emptyList());

        String viewName = machineryController.searchResults("", "", "", "", "", model);

        assertEquals("search", viewName);
        verify(machineryService).getAllMachinery();
    }

    // --- Tests para el Detalle (Cubriendo el redirect) ---

    @Test
    void testMachineryDetail_Found() {
        Long id = 1L;
        Machinery machinery = new Machinery();
        when(machineryService.getMachineryById(id)).thenReturn(Optional.of(machinery));

        String viewName = machineryController.machineryDetail(id, model);

        assertEquals("machinery-detail", viewName);
        verify(model).addAttribute("machinery", machinery);
    }

    @Test
    void testMachineryDetail_NotFound() {
        Long id = 99L;
        when(machineryService.getMachineryById(id)).thenReturn(Optional.empty());

        String viewName = machineryController.machineryDetail(id, model);

        assertEquals("redirect:/search", viewName);
        // Verifica que nunca se agregó el atributo
        verify(model, never()).addAttribute(eq("machinery"), any());
    }
}