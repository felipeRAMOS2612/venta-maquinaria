package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.repositories.MachineryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MachineryService {
    private final MachineryRepository machineryRepository;

    public MachineryService(MachineryRepository machineryRepository) {
        this.machineryRepository = machineryRepository;
    }

    public Machinery saveMachinery(Machinery machinery) {
        return machineryRepository.save(machinery);
    }

    public Optional<Machinery> getMachineryById(Long id) {
        return machineryRepository.findById(id);
    }

    public List<Machinery> getAllMachinery() {
        return machineryRepository.findAll();
    }
}
