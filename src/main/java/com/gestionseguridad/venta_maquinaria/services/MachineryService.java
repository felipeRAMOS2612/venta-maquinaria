package com.gestionseguridad.venta_maquinaria.services;

import com.gestionseguridad.venta_maquinaria.models.Machinery;
import com.gestionseguridad.venta_maquinaria.repositories.MachineryRepository;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MachineryService {

    private final MachineryRepository machineryRepository;

    public MachineryService(MachineryRepository machineryRepository) {
        this.machineryRepository = machineryRepository;
    }

    @PostConstruct
    public void initializeMachinery() {
        if (machineryRepository.count() == 0) {
            List<Machinery> machineries = Arrays.asList(
                Machinery.builder()
                    .name("Tractor John Deere 5075E")
                    .description("Tractor versátil ideal para labores agrícolas medianas. Perfecto para arado, siembra y cultivo.")
                    .brand("John Deere")
                    .model("5075E")
                    .year(2020)
                    .type(Machinery.MachineryType.TRACTOR)
                    .condition(Machinery.MachineryCondition.EXCELENTE)
                    .specifications(Arrays.asList("75 HP", "Transmisión sincronizada", "Toma de fuerza", "Dirección hidráulica"))
                    .operationInstructions("Verificar nivel de aceite antes del uso. Máxima velocidad 40 km/h. Revisar presión de llantas.")
                    .location("Región Metropolitana, Melipilla")
                    .dailyRentalPrice(45000.0)
                    .ownerName("Juan Pérez")
                    .ownerContact("+56 9 1234 5678")
                    .popularity(150)
                    .available(true)
                    .build(),
                    
                Machinery.builder()
                    .name("Cosechadora Case IH Axial Flow 6130")
                    .description("Cosechadora de cereales de alta capacidad. Ideal para trigo, avena, cebada y otros cultivos.")
                    .brand("Case IH")
                    .model("Axial Flow 6130")
                    .year(2019)
                    .type(Machinery.MachineryType.COSECHADORA)
                    .condition(Machinery.MachineryCondition.BUENA)
                    .specifications(Arrays.asList("230 HP", "Cabina con aire acondicionado", "Sistema de limpieza avanzado", "Capacidad tolva 4000L"))
                    .operationInstructions("Solo operadores con experiencia. Ajustar velocidad según cultivo. Revisar cribas antes de usar.")
                    .location("Región de O'Higgins, Rancagua")
                    .dailyRentalPrice(120000.0)
                    .ownerName("María González")
                    .ownerContact("+56 9 8765 4321")
                    .popularity(89)
                    .available(true)
                    .build(),
                    
                Machinery.builder()
                    .name("Sembradora Amazone Primera DMC 3000")
                    .description("Sembradora de precisión para siembra directa. Excelente para maíz, soja y otros cultivos en hilera.")
                    .brand("Amazone")
                    .model("Primera DMC 3000")
                    .year(2021)
                    .type(Machinery.MachineryType.SEMBRADORA)
                    .condition(Machinery.MachineryCondition.EXCELENTE)
                    .specifications(Arrays.asList("3 metros de labor", "Discos de corte", "Dosificación neumática", "12 surcos"))
                    .operationInstructions("Calibrar semilla antes de iniciar. Velocidad máxima 12 km/h. Verificar profundidad de siembra.")
                    .location("Región del Maule, Curicó")
                    .dailyRentalPrice(35000.0)
                    .ownerName("Carlos Morales")
                    .ownerContact("+56 9 5555 1234")
                    .popularity(67)
                    .available(true)
                    .build(),
                    
                Machinery.builder()
                    .name("Fumigadora Jacto Advance 2000")
                    .description("Fumigadora autopropulsada para aplicación de fitosanitarios. Alta precisión y eficiencia.")
                    .brand("Jacto")
                    .model("Advance 2000")
                    .year(2018)
                    .type(Machinery.MachineryType.FUMIGADORA)
                    .condition(Machinery.MachineryCondition.BUENA)
                    .specifications(Arrays.asList("Tanque 2000L", "Barras de 18m", "GPS incorporado", "Cabina pressurizada"))
                    .operationInstructions("Usar EPP completo. Calibrar boquillas según producto. No usar con viento >15 km/h.")
                    .location("Región de La Araucanía, Temuco")
                    .dailyRentalPrice(55000.0)
                    .ownerName("Ana Silva")
                    .ownerContact("+56 9 9999 0000")
                    .popularity(123)
                    .available(true)
                    .build(),
                    
                Machinery.builder()
                    .name("Excavadora Caterpillar 320")
                    .description("Excavadora hidráulica para trabajos de movimiento de tierra, drenajes y construcción rural.")
                    .brand("Caterpillar")
                    .model("320")
                    .year(2017)
                    .type(Machinery.MachineryType.EXCAVADORA)
                    .condition(Machinery.MachineryCondition.REGULAR)
                    .specifications(Arrays.asList("145 HP", "Peso 20 toneladas", "Alcance 9.5m", "Profundidad excavación 6.5m"))
                    .operationInstructions("Solo operadores certificados. Revisar niveles hidráulicos diarios. Mantener distancia de líneas eléctricas.")
                    .location("Región de Valparaíso, San Felipe")
                    .dailyRentalPrice(75000.0)
                    .ownerName("Roberto Díaz")
                    .ownerContact("+56 9 7777 8888")
                    .popularity(98)
                    .available(true)
                    .build(),
                    
                Machinery.builder()
                    .name("Arado Lemken Juwel 7")
                    .description("Arado reversible de 4 cuerpos. Ideal para preparación de suelo y volteo de rastrojos.")
                    .brand("Lemken")
                    .model("Juwel 7")
                    .year(2022)
                    .type(Machinery.MachineryType.ARADO)
                    .condition(Machinery.MachineryCondition.EXCELENTE)
                    .specifications(Arrays.asList("4 cuerpos", "Ancho de trabajo variable", "Rejas intercambiables", "Enganche tripuntal"))
                    .operationInstructions("Ajustar profundidad según suelo. Velocidad óptima 8-12 km/h. Revisar desgaste de rejas.")
                    .location("Región del Bío Bío, Chillán")
                    .dailyRentalPrice(25000.0)
                    .ownerName("Luis Herrera")
                    .ownerContact("+56 9 3333 4444")
                    .popularity(45)
                    .available(true)
                    .build()
            );
            
            machineryRepository.saveAll(machineries);
        }
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
    
    public List<Machinery> getRecentMachinery() {
        return machineryRepository.findAll().stream()
            .filter(Machinery::isRecent)
            .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
            .limit(6)
            .collect(Collectors.toList());
    }
    
    public List<Machinery> getPopularMachinery() {
        return machineryRepository.findAll().stream()
            .sorted((a, b) -> b.getPopularity().compareTo(a.getPopularity()))
            .limit(6)
            .collect(Collectors.toList());
    }
    
    public List<Machinery> searchMachineryByName(String name) {
        return machineryRepository.findAll().stream()
            .filter(machinery -> machinery.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<Machinery> getMachineryByType(Machinery.MachineryType type) {
        return machineryRepository.findAll().stream()
            .filter(machinery -> machinery.getType() == type)
            .collect(Collectors.toList());
    }
    
    public List<Machinery> getMachineryByLocation(String location) {
        return machineryRepository.findAll().stream()
            .filter(machinery -> machinery.getLocation().toLowerCase().contains(location.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<Machinery> getMachineryByBrand(String brand) {
        return machineryRepository.findAll().stream()
            .filter(machinery -> machinery.getBrand().toLowerCase().contains(brand.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    public List<Machinery> getAvailableMachinery() {
        return machineryRepository.findAll().stream()
            .filter(Machinery::isAvailable)
            .collect(Collectors.toList());
    }
}