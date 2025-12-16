package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Payment;
import com.gestionseguridad.venta_maquinaria.repositories.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void testSavePayment() {
        // Given
        Payment paymentToSave = new Payment(); 
        
        when(paymentRepository.save(any(Payment.class))).thenReturn(paymentToSave);

        // When
        Payment result = paymentService.savePayment(paymentToSave);

        // Then
        assertNotNull(result);
        verify(paymentRepository, times(1)).save(paymentToSave);
    }

    @Test
    void testGetPaymentById_Found() {
        // Given
        Long id = 1L;
        Payment payment = new Payment();
        when(paymentRepository.findById(id)).thenReturn(Optional.of(payment));

        // When
        Optional<Payment> result = paymentService.getPaymentById(id);

        // Then
        assertTrue(result.isPresent());
        assertEquals(payment, result.get());
        verify(paymentRepository, times(1)).findById(id);
    }

    @Test
    void testGetPaymentById_NotFound() {
        // Given
        Long id = 99L;
        when(paymentRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Payment> result = paymentService.getPaymentById(id);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllPayments() {
        // Given
        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
        when(paymentRepository.findAll()).thenReturn(payments);

        // When
        List<Payment> result = paymentService.getAllPayments();

        // Then
        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }
}