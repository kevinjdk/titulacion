package com.rce.titulacion.controller;

import com.rce.titulacion.model.Plato;
import com.rce.titulacion.payload.response.dto.*;
import com.rce.titulacion.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Necesario para el .stream()

@RestController
@RequestMapping("/api/platos")
@CrossOrigin(origins = "*", maxAge = 3600) // Configura CORS según tus necesidades
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    // Endpoint público: Obtener todos los platos
    // Ahora devuelve una lista de PlatoResponseDTO
    @GetMapping
    public ResponseEntity<List<PlatoResponseDTO>> getAllPlatos() {
        List<Plato> platos = platoService.findAllPlatos();
        // Mapea cada entidad Plato a un PlatoResponseDTO
        List<PlatoResponseDTO> dtoList = platos.stream()
                                                .map(PlatoResponseDTO::fromEntity)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint público: Obtener un plato por ID
    // Ahora devuelve un solo PlatoResponseDTO
    @GetMapping("/{id}")
    public ResponseEntity<PlatoResponseDTO> getPlatoById(@PathVariable Long id) {
        Optional<Plato> plato = platoService.findPlatoById(id);
        // Si el plato existe, lo mapea al DTO y lo devuelve; de lo contrario, 404
        return plato.map(p -> ResponseEntity.ok(PlatoResponseDTO.fromEntity(p)))
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint público: Buscar platos por nombre (ejemplo)
    // También devuelve una lista de PlatoResponseDTO
    @GetMapping("/search")
    public ResponseEntity<List<PlatoResponseDTO>> searchPlatos(@RequestParam String nombre) {
        List<Plato> platos = platoService.searchPlatosByNombre(nombre);
        if (platos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<PlatoResponseDTO> dtoList = platos.stream()
                                                .map(PlatoResponseDTO::fromEntity)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Endpoint protegido: Crear un nuevo plato (solo administrador)
    // Este método sigue recibiendo la entidad Plato directamente.
    // Si quisieras que el cliente envíe IDs y no objetos anidados,
    // podrías crear un PlatoRequestDTO con solo los IDs y luego mapearlo a la entidad.
    @PreAuthorize("isAuthenticated()") // Requiere que el usuario esté autenticado
    @PostMapping
    public ResponseEntity<Plato> createPlato(@RequestBody Plato plato) {
        // Al guardar, Hibernate solo necesita los IDs de las entidades relacionadas.
        // No necesita cargar los nombres para guardar.
        Plato newPlato = platoService.savePlato(plato);
        // Aquí puedes optar por devolver el Plato guardado directamente (como antes, con nulls en relacionados)
        // O podrías devolver un PlatoResponseDTO si quieres que la respuesta POST tenga los nombres.
        // Por simplicidad, se mantiene Plato para POST, ya que el objetivo principal son las consultas GET.
        return new ResponseEntity<>(newPlato, HttpStatus.CREATED);
    }

    // Endpoint protegido: Actualizar un plato existente (solo administrador)
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Plato> updatePlato(@PathVariable Long id, @RequestBody Plato plato) {
        return platoService.findPlatoById(id)
                .map(existingPlato -> {
                    plato.setId(id); // Asegura que el ID de la URL se use
                    Plato updatedPlato = platoService.savePlato(plato);
                    return ResponseEntity.ok(updatedPlato);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Endpoint protegido: Eliminar un plato (solo administrador)
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlato(@PathVariable Long id) {
        if (platoService.findPlatoById(id).isPresent()) {
            platoService.deletePlato(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}