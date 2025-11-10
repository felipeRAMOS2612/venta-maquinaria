package com.gestionseguridad.venta_maquinaria.repositories;

import com.gestionseguridad.venta_maquinaria.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
