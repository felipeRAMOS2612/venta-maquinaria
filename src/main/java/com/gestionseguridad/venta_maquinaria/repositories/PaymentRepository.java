package com.gestionseguridad.venta_maquinaria.repositories;

import com.gestionseguridad.venta_maquinaria.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
