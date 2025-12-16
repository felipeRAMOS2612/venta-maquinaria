package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Rental;
import com.gestionseguridad.venta_maquinaria.repositories.RentalRepository;
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
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void testSaveRental() {
        Rental rentalToSave = new Rental();
        when(rentalRepository.save(any(Rental.class))).thenReturn(rentalToSave);

        Rental result = rentalService.saveRental(rentalToSave);

        assertNotNull(result);
        verify(rentalRepository, times(1)).save(rentalToSave);
    }

    @Test
    void testGetRentalById_Found() {
        Long id = 1L;
        Rental rental = new Rental();
        when(rentalRepository.findById(id)).thenReturn(Optional.of(rental));

        Optional<Rental> result = rentalService.getRentalById(id);

        assertTrue(result.isPresent());
        assertEquals(rental, result.get());
        verify(rentalRepository, times(1)).findById(id);
    }

    @Test
    void testGetRentalById_NotFound() {
        Long id = 99L;
        when(rentalRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Rental> result = rentalService.getRentalById(id);

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllRentals() {
        List<Rental> rentals = Arrays.asList(new Rental(), new Rental());
        when(rentalRepository.findAll()).thenReturn(rentals);

        List<Rental> result = rentalService.getAllRentals();

        assertEquals(2, result.size());
        verify(rentalRepository, times(1)).findAll();
    }
}