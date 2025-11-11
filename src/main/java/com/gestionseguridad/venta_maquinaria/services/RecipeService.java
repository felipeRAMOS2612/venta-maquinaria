package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Recipe;
import com.gestionseguridad.venta_maquinaria.repositories.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecipeService {
    
    @Autowired
    private RecipeRepository recipeRepository;
    
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }
    
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }
    
    public List<Recipe> searchRecipesByName(String name) {
        return recipeRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Recipe> getRecipesByCuisineType(Recipe.CuisineType cuisineType) {
        return recipeRepository.findByCuisineType(cuisineType);
    }
    
    public List<Recipe> getRecipesByCountry(String country) {
        return recipeRepository.findByCountryContainingIgnoreCase(country);
    }
    
    public List<Recipe> getRecipesByDifficulty(Recipe.Difficulty difficulty) {
        return recipeRepository.findByDifficulty(difficulty);
    }
    
    public List<Recipe> searchRecipesByIngredient(String ingredient) {
        return recipeRepository.findByIngredientsContaining(ingredient);
    }
    
    public List<Recipe> getRecentRecipes() {
        return recipeRepository.findRecentRecipes();
    }
    
    public List<Recipe> getPopularRecipes() {
        return recipeRepository.findPopularRecipes();
    }
    
    public List<Recipe> getRecentAndPopularRecipes() {
        return recipeRepository.findRecentAndPopular();
    }
    
    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }
    
    public void initializeStaticData() {
        if (recipeRepository.count() == 0) {
            List<Recipe> sampleRecipes = List.of(
                Recipe.builder()
                    .name("Pasta Carbonara")
                    .description("Deliciosa pasta italiana con huevo y panceta")
                    .cuisineType(Recipe.CuisineType.ITALIANA)
                    .ingredients(List.of("Pasta", "Huevos", "Panceta", "Queso parmesano", "Pimienta negra"))
                    .instructions("1. Hervir la pasta al dente\n2. Freír la panceta\n3. Mezclar huevos con queso\n4. Combinar todo fuera del fuego")
                    .country("Italia")
                    .difficulty(Recipe.Difficulty.MEDIO)
                    .cookingTime(20)
                    .imageUrl("/images/carbonara.jpg")
                    .popularity(95)
                    .build(),
                    
                Recipe.builder()
                    .name("Tacos al Pastor")
                    .description("Auténticos tacos mexicanos con carne al pastor")
                    .cuisineType(Recipe.CuisineType.MEXICANA)
                    .ingredients(List.of("Tortillas", "Carne de cerdo", "Piña", "Cebolla", "Cilantro", "Salsa"))
                    .instructions("1. Marinar la carne\n2. Cocinar en trompo\n3. Cortar finamente\n4. Servir en tortillas con piña y cebolla")
                    .country("México")
                    .difficulty(Recipe.Difficulty.DIFICIL)
                    .cookingTime(45)
                    .imageUrl("/images/tacos.jpg")
                    .popularity(88)
                    .build(),
                    
                Recipe.builder()
                    .name("Sushi Rolls")
                    .description("Rollos de sushi frescos y deliciosos")
                    .cuisineType(Recipe.CuisineType.JAPONESA)
                    .ingredients(List.of("Arroz para sushi", "Nori", "Salmón", "Aguacate", "Pepino", "Wasabi"))
                    .instructions("1. Preparar arroz de sushi\n2. Extender nori\n3. Agregar ingredientes\n4. Enrollar firmemente")
                    .country("Japón")
                    .difficulty(Recipe.Difficulty.DIFICIL)
                    .cookingTime(30)
                    .imageUrl("/images/sushi.jpg")
                    .popularity(92)
                    .build(),
                    
                Recipe.builder()
                    .name("Ensalada César")
                    .description("Clásica ensalada con aderezo césar")
                    .cuisineType(Recipe.CuisineType.MEDITERRANEA)
                    .ingredients(List.of("Lechuga romana", "Crutones", "Queso parmesano", "Pollo", "Aderezo césar"))
                    .instructions("1. Lavar y cortar lechuga\n2. Preparar crutones\n3. Cocinar pollo\n4. Mezclar con aderezo")
                    .country("Estados Unidos")
                    .difficulty(Recipe.Difficulty.FACIL)
                    .cookingTime(15)
                    .imageUrl("/images/cesar.jpg")
                    .popularity(75)
                    .build(),
                    
                Recipe.builder()
                    .name("Paella Valenciana")
                    .description("Tradicional paella española con pollo y verduras")
                    .cuisineType(Recipe.CuisineType.ESPAÑOLA)
                    .ingredients(List.of("Arroz bomba", "Pollo", "Judías verdes", "Garrofón", "Tomate", "Azafrán"))
                    .instructions("1. Sofreír pollo\n2. Agregar verduras\n3. Añadir arroz y caldo\n4. Cocinar hasta que el arroz esté listo")
                    .country("España")
                    .difficulty(Recipe.Difficulty.DIFICIL)
                    .cookingTime(60)
                    .imageUrl("/images/paella.jpg")
                    .popularity(85)
                    .build()
            );
            
            recipeRepository.saveAll(sampleRecipes);
        }
    }
}