package com.rce.titulacion.controller;

import com.rce.titulacion.model.Provincia;
import com.rce.titulacion.exception.ResourceNotFoundException; // Importa la nueva excepción
import com.rce.titulacion.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rce.titulacion.payload.response.ApiResponse; // Importa el nuevo ApiResponse
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/provincias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    // Consulta pública
    @GetMapping // Ahora devuelve ApiResponse<List<Provincia>>
    public ResponseEntity<ApiResponse<List<Provincia>>> getAllProvincias() {
        List<Provincia> provincias = provinciaService.findAllProvincias();
        return ResponseEntity.ok(ApiResponse.success("Provincias recuperadas exitosamente", provincias));
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Provincia>> getProvinciaById(@PathVariable Long id) {
        Optional<Provincia> provincia = provinciaService.findProvinciaById(id);
        return provincia.map(p -> ResponseEntity.ok(ApiResponse.success("Provincia recuperada exitosamente", p)))
                        .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada con ID: " + id)); // Lanza la excepción
    }

    // Consulta pública de provincias por ID de región
    @GetMapping("/region/{regionId}")
    public ResponseEntity<ApiResponse<List<Provincia>>> getProvinciasByRegionId(@PathVariable Long regionId) {
        List<Provincia> provincias = provinciaService.findProvinciasByRegionId(regionId);
        if (provincias.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No se encontraron provincias para la región con ID: " + regionId, provincias)); // 200 OK con lista vacía
        }
        return ResponseEntity.ok(ApiResponse.success("Provincias para la región recuperadas exitosamente", provincias));
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Provincia>> createProvincia(@RequestBody Provincia provincia) {
        Provincia newProvincia = provinciaService.saveProvincia(provincia);
        return new ResponseEntity<>(ApiResponse.success("Provincia creada exitosamente", newProvincia), HttpStatus.CREATED);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Provincia>> updateProvincia(@PathVariable Long id, @RequestBody Provincia provincia) {
        return provinciaService.findProvinciaById(id)
                .map(existingProvincia -> {
                    provincia.setId(id);
                    Provincia updatedProvincia = provinciaService.saveProvincia(provincia);
                    return ResponseEntity.ok(ApiResponse.success("Provincia actualizada exitosamente", updatedProvincia));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada con ID: " + id)); // Lanza la excepción
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvincia(@PathVariable Long id) {
        if (provinciaService.findProvinciaById(id).isPresent()) {
            provinciaService.deleteProvincia(id);
            return ResponseEntity.ok(ApiResponse.success("Provincia eliminada exitosamente")); // 200 OK con mensaje de éxito
        }
        throw new ResourceNotFoundException("Provincia no encontrada con ID: " + id); // Lanza la excepción
    }
}