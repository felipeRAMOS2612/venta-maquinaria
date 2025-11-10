package com.gestionseguridad.venta_maquinaria.controllers;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.services.MachineryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/machinery")
public class MachineryController {
    private final MachineryService machineryService;

    public MachineryController(MachineryService machineryService) {
        this.machineryService = machineryService;
    }

    @GetMapping
    public List<Machinery> getAllMachinery() {
        return machineryService.getAllMachinery();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Machinery> getMachineryById(@PathVariable Long id) {
        return machineryService.getMachineryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Machinery createMachinery(@RequestBody Machinery machinery) {
        return machineryService.saveMachinery(machinery);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Machinery> updateMachinery(@RequestBody Machinery machinery, @PathVariable Long id) {
        return machineryService.getMachineryById(id)
                .map(existingMachinery -> {
                    machinery.setId(existingMachinery.getId());
                    return ResponseEntity.ok(machineryService.saveMachinery(machinery));
                }).orElse(ResponseEntity.notFound().build());
    }
}
