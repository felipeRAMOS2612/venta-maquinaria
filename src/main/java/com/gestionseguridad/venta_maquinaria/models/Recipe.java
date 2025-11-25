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
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private CuisineType cuisineType;
    
    @ElementCollection
    @CollectionTable(name = "recipe_ingredients")
    private List<String> ingredients;
    
    @Column(columnDefinition = "TEXT")
    private String instructions;
    
    private String country;
    
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    
    private Integer cookingTime;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Integer popularity;
    private boolean isRecent;
    
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.isRecent = true;
        this.popularity = 0;
    }
    
    public enum CuisineType {
        ITALIANA, MEXICANA, CHINA, JAPONESA, FRANCESA, MEDITERRANEA, PERUANA, THAI, INDIA, ESPANOLA
    }
    
    public enum Difficulty {
        FACIL, MEDIO, DIFICIL
    }
}