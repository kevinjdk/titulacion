package com.rce.titulacion.controller;

import com.rce.titulacion.model.Provincia;
import com.rce.titulacion.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/provincias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    // Consulta pública
    @GetMapping
    public ResponseEntity<List<Provincia>> getAllProvincias() {
        List<Provincia> provincias = provinciaService.findAllProvincias();
        return ResponseEntity.ok(provincias);
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<Provincia> getProvinciaById(@PathVariable Long id) {
        Optional<Provincia> provincia = provinciaService.findProvinciaById(id);
        return provincia.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Consulta pública de provincias por ID de región
    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<Provincia>> getProvinciasByRegionId(@PathVariable Long regionId) {
        List<Provincia> provincias = provinciaService.findProvinciasByRegionId(regionId);
        if (provincias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(provincias);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Provincia> createProvincia(@RequestBody Provincia provincia) {
        Provincia newProvincia = provinciaService.saveProvincia(provincia);
        return new ResponseEntity<>(newProvincia, HttpStatus.CREATED);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Provincia> updateProvincia(@PathVariable Long id, @RequestBody Provincia provincia) {
        return provinciaService.findProvinciaById(id)
                .map(existingProvincia -> {
                    provincia.setId(id);
                    Provincia updatedProvincia = provinciaService.saveProvincia(provincia);
                    return ResponseEntity.ok(updatedProvincia);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvincia(@PathVariable Long id) {
        if (provinciaService.findProvinciaById(id).isPresent()) {
            provinciaService.deleteProvincia(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}