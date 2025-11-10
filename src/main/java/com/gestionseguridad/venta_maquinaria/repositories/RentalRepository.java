package com.gestionseguridad.venta_maquinaria.repositories;

import com.gestionseguridad.venta_maquinaria.models.Rental;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {

}
