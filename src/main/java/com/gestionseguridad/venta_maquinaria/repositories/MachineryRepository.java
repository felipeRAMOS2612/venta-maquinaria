package com.gestionseguridad.venta_maquinaria.repositories;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MachineryRepository extends JpaRepository<Machinery, Long> {

}
