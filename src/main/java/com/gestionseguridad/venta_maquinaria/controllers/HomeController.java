package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final MachineryService machineryService;

    public HomeController(MachineryService machineryService) {
        this.machineryService = machineryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println("Accessing home page");

        model.addAttribute("recentMachineries", machineryService.getRecentMachinery());
        model.addAttribute("popularMachineries", machineryService.getPopularMachinery());
        return "index";
    }
}