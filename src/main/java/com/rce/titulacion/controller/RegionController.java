package com.rce.titulacion.controller;

import com.rce.titulacion.model.Region;
import com.rce.titulacion.exception.ResourceNotFoundException;
import com.rce.titulacion.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rce.titulacion.payload.response.ApiResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/regiones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegionController {

    @Autowired
    private RegionService regionService;

    // Consulta pública
    @GetMapping // Ahora devuelve ApiResponse<List<Region>>
    public ResponseEntity<ApiResponse<List<Region>>> getAllRegiones() {
        List<Region> regiones = regionService.findAllRegiones();
        return ResponseEntity.ok(ApiResponse.success("Regiones recuperadas exitosamente", regiones));
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Region>> getRegionById(@PathVariable Long id) {
        Optional<Region> region = regionService.findRegionById(id);
        return region.map(r -> ResponseEntity.ok(ApiResponse.success("Región recuperada exitosamente", r)))
                     .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada con ID: " + id));
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Region>> createRegion(@RequestBody Region region) {
        Region newRegion = regionService.saveRegion(region);
        return new ResponseEntity<>(ApiResponse.success("Región creada exitosamente", newRegion), HttpStatus.CREATED);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Region>> updateRegion(@PathVariable Long id, @RequestBody Region region) {
        return regionService.findRegionById(id)
                .map(existingRegion -> {
                    region.setId(id);
                    Region updatedRegion = regionService.saveRegion(region);
                    return ResponseEntity.ok(ApiResponse.success("Región actualizada exitosamente", updatedRegion));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada con ID: " + id));
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable Long id) {
        if (regionService.findRegionById(id).isPresent()) {
            regionService.deleteRegion(id);
            return ResponseEntity.ok(ApiResponse.success("Región eliminada exitosamente"));
        }
        throw new ResourceNotFoundException("Región no encontrada con ID: " + id);
    }
}