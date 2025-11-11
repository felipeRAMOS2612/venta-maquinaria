package com.gestionseguridad.venta_maquinaria.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Machinery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String brand;
    private String model;
    private Integer year;
    
    @Enumerated(EnumType.STRING)
    private MachineryType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "machinery_condition")
    private MachineryCondition condition;
    
    @ElementCollection
    @CollectionTable(name = "machinery_specifications")
    private List<String> specifications;
    
    @Column(columnDefinition = "TEXT")
    private String operationInstructions;
    
    private String location;
    private Double dailyRentalPrice;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Integer popularity;
    private boolean isRecent;
    private boolean available;
    
    // Información del dueño
    private String ownerName;
    private String ownerContact;
    
    @ManyToOne
    private User owner;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isRecent = true;
        this.popularity = 0;
        this.available = true;
    }
    
    public enum MachineryType {
        TRACTOR, COSECHADORA, SEMBRADORA, CULTIVADOR, ARADO, FUMIGADORA, 
        SEGADORA, RASTRA, EMPACADORA, EXCAVADORA, MOTONIVELADORA, RIEGO
    }
    
    public enum MachineryCondition {
        EXCELENTE, BUENA, REGULAR, NECESITA_MANTENIMIENTO
    }
}
