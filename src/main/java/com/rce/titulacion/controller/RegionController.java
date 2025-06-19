package com.rce.titulacion.controller;

import com.rce.titulacion.model.Region;
import com.rce.titulacion.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regiones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegionController {

    @Autowired
    private RegionService regionService;

    // Consulta pública
    @GetMapping
    public ResponseEntity<List<Region>> getAllRegiones() {
        List<Region> regiones = regionService.findAllRegiones();
        return ResponseEntity.ok(regiones);
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        Optional<Region> region = regionService.findRegionById(id);
        return region.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Region> createRegion(@RequestBody Region region) {
        Region newRegion = regionService.saveRegion(region);
        return new ResponseEntity<>(newRegion, HttpStatus.CREATED);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable Long id, @RequestBody Region region) {
        return regionService.findRegionById(id)
                .map(existingRegion -> {
                    region.setId(id);
                    Region updatedRegion = regionService.saveRegion(region);
                    return ResponseEntity.ok(updatedRegion);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        if (regionService.findRegionById(id).isPresent()) {
            regionService.deleteRegion(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}