package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Recipe;
import com.gestionseguridad.venta_maquinaria.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("recipes", recipeService.getAllRecipes());
        model.addAttribute("searchPerformed", false);
        return "search";
    }

    @GetMapping("/search/results")
    public String searchResults(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String cuisineType,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String difficulty,
            Model model) {

        List<Recipe> recipes;

        if (name != null && !name.isEmpty()) {
            recipes = recipeService.searchRecipesByName(name);
        } else if (cuisineType != null && !cuisineType.isEmpty()) {
            recipes = recipeService.getRecipesByCuisineType(Recipe.CuisineType.valueOf(cuisineType));
        } else if (ingredient != null && !ingredient.isEmpty()) {
            recipes = recipeService.searchRecipesByIngredient(ingredient);
        } else if (country != null && !country.isEmpty()) {
            recipes = recipeService.getRecipesByCountry(country);
        } else if (difficulty != null && !difficulty.isEmpty()) {
            recipes = recipeService.getRecipesByDifficulty(Recipe.Difficulty.valueOf(difficulty));
        } else {
            recipes = recipeService.getAllRecipes();
        }

        model.addAttribute("recipes", recipes);
        model.addAttribute("searchPerformed", true);
        
        return "search";
    }

    @GetMapping("/recipe/{id}")
    public String recipeDetail(@PathVariable Long id, Model model) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        if (recipe.isPresent()) {
            model.addAttribute("recipe", recipe.get());
            return "recipe-detail";
        } else {
            return "redirect:/search";
        }
    }
}