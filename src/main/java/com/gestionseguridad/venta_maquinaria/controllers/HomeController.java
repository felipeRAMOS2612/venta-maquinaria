package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private RecipeService recipeService;
    
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recentRecipes", recipeService.getRecentRecipes().stream().limit(6).toList());
        model.addAttribute("popularRecipes", recipeService.getPopularRecipes().stream().limit(6).toList());
        return "index";
    }
}
