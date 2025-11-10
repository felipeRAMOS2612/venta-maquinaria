package com.gestionseguridad.venta_maquinaria.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Machinery machinery;

    @ManyToOne
    private User tenant;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
