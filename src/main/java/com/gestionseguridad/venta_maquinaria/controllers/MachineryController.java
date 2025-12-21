package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.models.User;
import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import com.gestionseguridad.venta_maquinaria.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MachineryController {

    private final MachineryService machineryService;
    private final UserService userService; 

    public MachineryController(MachineryService machineryService, UserService userService) {
        this.machineryService = machineryService;
        this.userService = userService;
    }

    // ==========================================
    // MÉTODOS DE BÚSQUEDA (LOS QUE FALTABAN)
    // ==========================================

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

    // ==========================================
    // NUEVOS MÉTODOS (CREAR, EDITAR, GUARDAR)
    // ==========================================

    @GetMapping("/machinery/new")
    public String createMachineryForm(Model model) {
        model.addAttribute("machinery", new Machinery());
        model.addAttribute("types", Machinery.MachineryType.values()); 
        model.addAttribute("conditions", Machinery.MachineryCondition.values()); 
        model.addAttribute("isEdit", false);
        return "machinery-form";
    }

    @GetMapping("/machinery/edit/{id}")
    public String editMachineryForm(@PathVariable Long id, Model model) {
        Optional<Machinery> machinery = machineryService.getMachineryById(id);
        
        if (machinery.isPresent()) {
            model.addAttribute("machinery", machinery.get());
            model.addAttribute("types", Machinery.MachineryType.values());
            model.addAttribute("conditions", Machinery.MachineryCondition.values());
            model.addAttribute("isEdit", true);
            return "machinery-form";
        } else {
            return "redirect:/search?error=MaquinariaNoEncontrada";
        }
    }

    @PostMapping("/machinery/save")
    public String saveMachinery(@ModelAttribute Machinery machinery) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.getUserByEmail(email);

        if (currentUser != null) {
            machinery.setOwner(currentUser);
            machinery.setOwnerName(currentUser.getName());
            machinery.setOwnerContact(machinery.getOwnerContact() != null ? machinery.getOwnerContact() : currentUser.getEmail());
        }

        if (machinery.getId() != null) {
            Optional<Machinery> existing = machineryService.getMachineryById(machinery.getId());
            existing.ifPresent(val -> machinery.setCreatedAt(val.getCreatedAt()));
        }

        machineryService.saveMachinery(machinery);
        return "redirect:/dashboard?success=MaquinariaGuardada";
    }
    
    @GetMapping("/machinery/delete/{id}")
    public String deleteMachinery(@PathVariable Long id) {
        return "redirect:/dashboard";
    }
}