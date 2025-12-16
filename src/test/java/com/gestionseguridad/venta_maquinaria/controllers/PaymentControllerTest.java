package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Payment;
import com.gestionseguridad.venta_maquinaria.services.PaymentService;
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
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void testGetAllPayments() {
        // Given
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentService.getAllPayments()).thenReturn(payments);

        // When
        List<Payment> result = paymentController.getAllPayments();

        // Then
        assertEquals(2, result.size());
        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testGetPaymentById_Found() {
        // Given
        Long id = 1L;
        Payment payment = new Payment();
        payment.setId(id);
        when(paymentService.getPaymentById(id)).thenReturn(Optional.of(payment));

        // When
        ResponseEntity<Payment> response = paymentController.getPaymentById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void testGetPaymentById_NotFound() {
        // Given
        Long id = 99L;
        when(paymentService.getPaymentById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Payment> response = paymentController.getPaymentById(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreatePayment() {
        // Given
        Payment payment = new Payment();
        when(paymentService.savePayment(any(Payment.class))).thenReturn(payment);

        // When
        Payment result = paymentController.createPayment(payment);

        // Then
        assertNotNull(result);
        verify(paymentService, times(1)).savePayment(any(Payment.class));
    }

    @Test
    void testUpdatePayment_Found() {
        // Este test cubre cuando el pago si existe (HttpStatus 200)
        // Given
        Long id = 1L;
        Payment existingPayment = new Payment();
        existingPayment.setId(id);
        
        Payment paymentUpdates = new Payment();
        
        when(paymentService.getPaymentById(id)).thenReturn(Optional.of(existingPayment));
        // Simula que al guardar retorna el objeto actualizado
        when(paymentService.savePayment(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        ResponseEntity<Payment> response = paymentController.updatePayment(paymentUpdates, id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId()); // Verifica que mantuvo el ID correcto
    }

    @Test
    void testUpdatePayment_NotFound() {
        // Este test cubre el ".orElse(notFound())" (HttpStatus 404)
        // Given
        Long id = 99L;
        Payment paymentUpdates = new Payment();
        
        when(paymentService.getPaymentById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Payment> response = paymentController.updatePayment(paymentUpdates, id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // Verifica que no se llamó a guardar, protegiendo la integridad de datos
        verify(paymentService, never()).savePayment(any());
    }
}