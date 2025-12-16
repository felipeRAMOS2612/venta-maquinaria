package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Rental;
import com.gestionseguridad.venta_maquinaria.services.RentalService;
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
class RentalControllerTest {

    @Mock
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    @Test
    void testGetAllRentals() {
        // Given
        List<Rental> rentals = Arrays.asList(new Rental(), new Rental());
        when(rentalService.getAllRentals()).thenReturn(rentals);

        // When
        List<Rental> result = rentalController.getAllRentals();

        // Then
        assertEquals(2, result.size());
        verify(rentalService, times(1)).getAllRentals();
    }

    @Test
    void testGetRentalById_Found() {
        // Given
        Long id = 1L;
        Rental rental = new Rental();
        rental.setId(id);
        when(rentalService.getRentalById(id)).thenReturn(Optional.of(rental));

        // When
        ResponseEntity<Rental> response = rentalController.getRentalById(id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void testGetRentalById_NotFound() {
        // Given
        Long id = 99L;
        when(rentalService.getRentalById(id)).thenReturn(Optional.empty());

        // When
        ResponseEntity<Rental> response = rentalController.getRentalById(id);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testCreateRental() {
        // Given
        Rental rental = new Rental();
        when(rentalService.saveRental(any(Rental.class))).thenReturn(rental);

        // When
        Rental result = rentalController.createRental(rental);

        // Then
        assertNotNull(result);
        verify(rentalService, times(1)).saveRental(any(Rental.class));
    }

    @Test
    void testUpdateRental_Found() {
        // Este test cubre la parte del ".map"
        // Given
        Long id = 1L;
        Rental existingRental = new Rental();
        existingRental.setId(id);
        
        Rental rentalUpdates = new Rental();
        
        when(rentalService.getRentalById(id)).thenReturn(Optional.of(existingRental));
        // Simula que al guardar devuelve el objeto actualizado
        when(rentalService.saveRental(any(Rental.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ResponseEntity<Rental> response = rentalController.updateRental(rentalUpdates, id);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
    }

    @Test
    void testUpdateRental_NotFound() {
        Long id = 99L;
        Rental rentalUpdates = new Rental();
        
        when(rentalService.getRentalById(id)).thenReturn(Optional.empty());

        ResponseEntity<Rental> response = rentalController.updateRental(rentalUpdates, id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(rentalService, never()).saveRental(any());
    }
}