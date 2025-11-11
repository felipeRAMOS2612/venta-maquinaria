package com.gestionseguridad.venta_maquinaria.repositories;

import com.gestionseguridad.venta_maquinaria.models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    List<Recipe> findByNameContainingIgnoreCase(String name);
    
    List<Recipe> findByCuisineType(Recipe.CuisineType cuisineType);
    
    List<Recipe> findByCountryContainingIgnoreCase(String country);
    
    List<Recipe> findByDifficulty(Recipe.Difficulty difficulty);
    
    @Query("SELECT r FROM Recipe r JOIN r.ingredients i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :ingredient, '%'))")
    List<Recipe> findByIngredientsContaining(@Param("ingredient") String ingredient);
    
    @Query("SELECT r FROM Recipe r ORDER BY r.createdAt DESC")
    List<Recipe> findRecentRecipes();
    
    @Query("SELECT r FROM Recipe r ORDER BY r.popularity DESC")
    List<Recipe> findPopularRecipes();
    
    @Query("SELECT r FROM Recipe r WHERE r.isRecent = true ORDER BY r.createdAt DESC")
    List<Recipe> findRecentAndPopular();
}