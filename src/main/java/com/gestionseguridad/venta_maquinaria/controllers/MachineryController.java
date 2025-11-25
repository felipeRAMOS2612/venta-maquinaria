package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class MachineryController {

    @Autowired
    private MachineryService machineryService;

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("machineries", machineryService.getAllMachinery());
        model.addAttribute("searchPerformed", false);
        return "search";
    }

    @GetMapping("/search/results")
    public String searchResults(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String condition,
            Model model) {

        List<Machinery> machineries;

        if (name != null && !name.isEmpty()) {
            machineries = machineryService.searchMachineryByName(name);
        } else if (type != null && !type.isEmpty()) {
            machineries = machineryService.getMachineryByType(Machinery.MachineryType.valueOf(type));
        } else if (brand != null && !brand.isEmpty()) {
            machineries = machineryService.getMachineryByBrand(brand);
        } else if (location != null && !location.isEmpty()) {
            machineries = machineryService.getMachineryByLocation(location);
        } else {
            machineries = machineryService.getAllMachinery();
        }

        model.addAttribute("machineries", machineries);
        model.addAttribute("searchPerformed", true);
        
        return "search";
    }

    @GetMapping("/machinery/{id}")
    public String machineryDetail(@PathVariable Long id, Model model) {
        Optional<Machinery> machinery = machineryService.getMachineryById(id);
        if (machinery.isPresent()) {
            model.addAttribute("machinery", machinery.get());
            return "machinery-detail";
        } else {
            return "redirect:/search";
        }
    }
}
