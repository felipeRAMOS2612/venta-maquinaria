package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
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
    private UserService userService; // Mockeamos el nuevo servicio

    @Mock
    private Model model;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MachineryController machineryController;

    // Métodos de búsqueda (Ya existentes, los mantenemos para el coverage)
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
        String name = "Tractor";
        when(machineryService.searchMachineryByName(name)).thenReturn(Collections.emptyList());
        String viewName = machineryController.searchResults(name, null, null, null, null, model);
        assertEquals("search", viewName);
        verify(machineryService).searchMachineryByName(name);
        verify(model).addAttribute("searchPerformed", true);
    }

    @Test
    void testSearchResults_ByType_WhenNameIsEmpty() {
        String name = ""; 
        String type = "TRACTOR";
        when(machineryService.getMachineryByType(any())).thenReturn(Collections.emptyList());
        String viewName = machineryController.searchResults(name, type, null, null, null, model);
        assertEquals("search", viewName);
        verify(machineryService).getMachineryByType(any());
    }

    @Test
    void testSearchResults_ByBrand() {
        String brand = "CAT";
        when(machineryService.getMachineryByBrand(brand)).thenReturn(Collections.emptyList());
        String viewName = machineryController.searchResults("", "", brand, null, null, model);
        assertEquals("search", viewName);
        verify(machineryService).getMachineryByBrand(brand);
    }

    @Test
    void testSearchResults_ByLocation() {
        String location = "Santiago";
        when(machineryService.getMachineryByLocation(location)).thenReturn(Collections.emptyList());
        String viewName = machineryController.searchResults("", "", "", location, null, model);
        assertEquals("search", viewName);
        verify(machineryService).getMachineryByLocation(location);
    }

    @Test
    void testSearchResults_AllEmpty() {
        when(machineryService.getAllMachinery()).thenReturn(Collections.emptyList());
        String viewName = machineryController.searchResults("", "", "", "", "", model);
        assertEquals("search", viewName);
        verify(machineryService).getAllMachinery();
    }

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
    }

    // ==========================================
    // NUEVOS TESTS (PARA EL 100% COVERAGE)
    // ==========================================

    @Test
    void testCreateMachineryForm() {
        String viewName = machineryController.createMachineryForm(model);
        
        assertEquals("machinery-form", viewName);
        // Verifica que se agregan los atributos necesarios
        verify(model).addAttribute(eq("machinery"), any(Machinery.class));
        verify(model).addAttribute(eq("types"), any());
        verify(model).addAttribute(eq("conditions"), any());
        verify(model).addAttribute("isEdit", false);
    }

    @Test
    void testEditMachineryForm_Found() {
        Long id = 1L;
        Machinery machinery = new Machinery();
        when(machineryService.getMachineryById(id)).thenReturn(Optional.of(machinery));

        String viewName = machineryController.editMachineryForm(id, model);

        assertEquals("machinery-form", viewName);
        verify(model).addAttribute("machinery", machinery);
        verify(model).addAttribute("isEdit", true);
    }

    @Test
    void testEditMachineryForm_NotFound() {
        Long id = 99L;
        when(machineryService.getMachineryById(id)).thenReturn(Optional.empty());

        String viewName = machineryController.editMachineryForm(id, model);

        assertEquals("redirect:/search?error=MaquinariaNoEncontrada", viewName);
    }

    @Test
    void testSaveMachinery_New() {
        // Simulamos la autenticación
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("owner@test.com");

        // Simulamos el usuario logueado
        User owner = new User();
        owner.setEmail("owner@test.com");
        owner.setName("Owner Name");
        when(userService.getUserByEmail("owner@test.com")).thenReturn(owner);

        Machinery machinery = new Machinery(); // ID nulo -> Nuevo

        String viewName = machineryController.saveMachinery(machinery);

        assertEquals("redirect:/dashboard?success=MaquinariaGuardada", viewName);
        
        // Verifica que se asignó el dueño
        assertEquals(owner, machinery.getOwner());
        assertEquals("Owner Name", machinery.getOwnerName());
        assertEquals("owner@test.com", machinery.getOwnerContact()); // Contacto por defecto
        
        verify(machineryService).saveMachinery(machinery);
    }

    @Test
    void testSaveMachinery_Edit_PreserveDate() {
        // Simulamos autenticación
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("owner@test.com");
        when(userService.getUserByEmail("owner@test.com")).thenReturn(null); // Caso borde: usuario no encontrado

        // Maquinaria existente con fecha antigua
        LocalDateTime oldDate = LocalDateTime.of(2020, 1, 1, 12, 0);
        Machinery existingMachinery = new Machinery();
        existingMachinery.setId(10L);
        existingMachinery.setCreatedAt(oldDate);

        // Maquinaria que viene del form (sin fecha)
        Machinery formMachinery = new Machinery();
        formMachinery.setId(10L);

        when(machineryService.getMachineryById(10L)).thenReturn(Optional.of(existingMachinery));

        String viewName = machineryController.saveMachinery(formMachinery);

        assertEquals("redirect:/dashboard?success=MaquinariaGuardada", viewName);
        
        assertEquals(oldDate, formMachinery.getCreatedAt());
        
        verify(machineryService).saveMachinery(formMachinery);
    }

    @Test
    void testDeleteMachinery() {
        Long id = 5L;
        String viewName = machineryController.deleteMachinery(id);
        assertEquals("redirect:/dashboard", viewName);
    }
}