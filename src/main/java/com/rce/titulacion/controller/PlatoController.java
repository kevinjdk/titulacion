package com.rce.titulacion.controller;

import com.rce.titulacion.model.Plato;
import com.rce.titulacion.exception.ResourceNotFoundException;
import com.rce.titulacion.payload.response.ApiResponse;
import com.rce.titulacion.payload.response.dto.*;
import com.rce.titulacion.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de platos.
 * 
 * Este controlador proporciona endpoints para realizar operaciones CRUD
 * sobre las entidades Plato, incluyendo consultas públicas y operaciones
 * administrativas que requieren autenticación.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/platos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlatoController {

    @Autowired
    private PlatoService platoService;

    /**
     * Obtiene todos los platos disponibles.
     * 
     * Este endpoint público devuelve una lista completa de todos los platos
     * registrados en el sistema, incluyendo información detallada como
     * categoría, provincia y región a través de DTOs.
     * 
     * @return ResponseEntity conteniendo ApiResponse con la lista de PlatoResponseDTO
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PlatoResponseDTO>>> getAllPlatos() {
        List<Plato> platos = platoService.findAllPlatos();
        List<PlatoResponseDTO> dtoList = platos.stream()
                                                .map(PlatoResponseDTO::fromEntity)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Platos recuperados exitosamente", dtoList));
    }

    /**
     * Obtiene un plato específico por su ID.
     * 
     * Este endpoint público permite recuperar los detalles completos de un plato
     * utilizando su identificador único. Si el plato no existe, se lanza una
     * excepción ResourceNotFoundException.
     * 
     * @param id El identificador único del plato a buscar
     * @return ResponseEntity conteniendo ApiResponse con el PlatoResponseDTO encontrado
     * @throws ResourceNotFoundException si no se encuentra un plato con el ID especificado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlatoResponseDTO>> getPlatoById(@PathVariable("id") Long id) {
        Optional<Plato> plato = platoService.findPlatoById(id);
        return plato.map(p -> ResponseEntity.ok(ApiResponse.success("Plato recuperado exitosamente", PlatoResponseDTO.fromEntity(p))))
                     .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con ID: " + id));
    }

    /**
     * Busca platos por nombre.
     * 
     * Este endpoint público permite buscar platos que contengan el término
     * especificado en su nombre. La búsqueda no es sensible a mayúsculas y
     * minúsculas y puede devolver múltiples resultados.
     * 
     * @param nombre El término de búsqueda para filtrar platos por nombre
     * @return ResponseEntity conteniendo ApiResponse con la lista de PlatoResponseDTO que coinciden
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PlatoResponseDTO>>> searchPlatos(@RequestParam String nombre) {
        List<Plato> platos = platoService.searchPlatosByNombre(nombre);
        if (platos.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No se encontraron platos con el nombre: " + nombre, List.of()));
        }
        List<PlatoResponseDTO> dtoList = platos.stream()
                                                .map(PlatoResponseDTO::fromEntity)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Platos encontrados por nombre recuperados exitosamente", dtoList));
    }

    /**
     * Crea un nuevo plato en el sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados crear nuevos platos.
     * El plato debe incluir toda la información requerida como nombre, descripción,
     * ingredientes, preparación, y las referencias a categoría, provincia y región.
     * 
     * @param plato La entidad Plato a crear con todos los datos necesarios
     * @return ResponseEntity conteniendo ApiResponse con el Plato creado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Plato>> createPlato(@RequestBody Plato plato) {
        Plato newPlato = platoService.savePlato(plato);
        return new ResponseEntity<>(ApiResponse.success("Plato creado exitosamente", newPlato), HttpStatus.CREATED);
    }

    /**
     * Actualiza un plato existente.
     * 
     * Este endpoint protegido permite a usuarios autenticados modificar
     * los datos de un plato existente. El ID del plato a actualizar se
     * especifica en la URL y debe coincidir con un plato existente.
     * 
     * @param id El identificador único del plato a actualizar
     * @param plato La entidad Plato con los datos actualizados
     * @return ResponseEntity conteniendo ApiResponse con el Plato actualizado
     * @throws ResourceNotFoundException si no se encuentra un plato con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Plato>> updatePlato(@PathVariable("id") Long id, @RequestBody Plato plato) {
        return platoService.findPlatoById(id)
                .map(existingPlato -> {
                    plato.setId(id);
                    Plato updatedPlato = platoService.savePlato(plato);
                    return ResponseEntity.ok(ApiResponse.success("Plato actualizado exitosamente", updatedPlato));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Plato no encontrado con ID: " + id));
    }

    /**
     * Elimina un plato del sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados eliminar
     * un plato específico del sistema utilizando su ID. La eliminación
     * es permanente y no se puede deshacer.
     * 
     * @param id El identificador único del plato a eliminar
     * @return ResponseEntity conteniendo ApiResponse con mensaje de confirmación
     * @throws ResourceNotFoundException si no se encuentra un plato con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlato(@PathVariable("id") Long id) {
        if (platoService.findPlatoById(id).isPresent()) {
            platoService.deletePlato(id);
            return ResponseEntity.ok(ApiResponse.success("Plato eliminado exitosamente"));
        }
        throw new ResourceNotFoundException("Plato no encontrado con ID: " + id);
    }
}