package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private AuthController authController;

    @Test
    void testLoginPage_Normal() {
        // Caso: Entrada normal sin parámetros (primera vez que entra)
        String viewName = authController.loginPage(null, null, model);

        assertEquals("login", viewName);
        // Verificamos que no se agregaron mensajes al modelo
        verify(model, never()).addAttribute(eq("errorMessage"), any());
        verify(model, never()).addAttribute(eq("logoutMessage"), any());
    }

    @Test
    void testLoginPage_WithError() {
        // Caso: Login fallido (url termina en ?error=true)
        String viewName = authController.loginPage("true", null, model);

        assertEquals("login", viewName);
        // Verificamos que se agregó el mensaje de error
        verify(model).addAttribute(eq("errorMessage"), anyString());
    }

    @Test
    void testLoginPage_WithLogout() {
        // Caso: Logout exitoso (url termina en ?logout=true)
        String viewName = authController.loginPage(null, "true", model);

        assertEquals("login", viewName);
        // Verificamos que se agregó el mensaje de despedida
        verify(model).addAttribute(eq("logoutMessage"), anyString());
    }

    @Test
    void testDashboard() {
        // Test simple para asegurar que retorna la vista correcta
        String viewName = authController.dashboard();
        assertEquals("dashboard", viewName);
    }

    @Test
    void testRegisterPage() {
        // Verifica que se carga el formulario de registro con un usuario vacío
        String viewName = authController.registerPage(model);

        assertEquals("register", viewName);
        // Debe inicializar un 'new User()' para el formulario
        verify(model).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void testRegisterUser_Success() {
        // Caso: Registro exitoso (entra al try)
        User user = new User();
        // Simula que el servicio guarda sin problemas
        when(userService.saveUser(any(User.class))).thenReturn(user);

        String viewName = authController.registerUser(user, model);

        // Debe redirigir al login
        assertEquals("login", viewName);
        verify(userService).saveUser(user);
        verify(model).addAttribute(eq("successMessage"), anyString());
    }

    @Test
    void testRegisterUser_Failure() {
        // Caso: Falla el registro (entra al catch)
        User user = new User();
        // Simula que el servicio falla (lanza excepción)
        doThrow(new RuntimeException("Error simulado")).when(userService).saveUser(any(User.class));

        String viewName = authController.registerUser(user, model);

        // Debe mantenerse en la página de registro y mostrar el error
        assertEquals("register", viewName);
        verify(model).addAttribute(eq("errorMessage"), contains("Error simulado"));
    }
}