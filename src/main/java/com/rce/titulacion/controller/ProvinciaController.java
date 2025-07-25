package com.rce.titulacion.controller;

import com.rce.titulacion.model.Provincia;
import com.rce.titulacion.dto.ProvinciaResponseDTO;
import com.rce.titulacion.exception.ResourceNotFoundException;
import com.rce.titulacion.service.ProvinciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rce.titulacion.payload.response.ApiResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de provincias.
 * 
 * Este controlador proporciona endpoints para realizar operaciones CRUD
 * sobre las entidades Provincia, incluyendo consultas públicas para obtener
 * información de provincias (tanto individuales como agrupadas por región)
 * y operaciones administrativas que requieren autenticación para crear,
 * actualizar y eliminar provincias.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/provincias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProvinciaController {

    @Autowired
    private ProvinciaService provinciaService;

    /**
     * Obtiene todas las provincias disponibles.
     * 
     * Este endpoint público devuelve una lista completa de todas las provincias
     * registradas en el sistema. Las provincias están organizadas por regiones
     * y son utilizadas para clasificar geográficamente los platos.
     * 
     * @return ResponseEntity conteniendo ApiResponse con la lista de Provincia
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProvinciaResponseDTO>>> getAllProvincias() {
        List<Provincia> provincias = provinciaService.findAllProvincias();
        List<ProvinciaResponseDTO> response = provincias.stream()
            .map(p -> new ProvinciaResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getRegion() != null 
                    ? new ProvinciaResponseDTO.RegionSummaryDTO(p.getRegion().getId(), p.getRegion().getNombre())
                    : null
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Provincias recuperadas exitosamente", response));
    }

    /**
     * Obtiene una provincia específica por su ID.
     * 
     * Este endpoint público permite recuperar los detalles de una provincia
     * utilizando su identificador único. Si la provincia no existe, se lanza
     * una excepción ResourceNotFoundException.
     * 
     * @param id El identificador único de la provincia a buscar
     * @return ResponseEntity conteniendo ApiResponse con la Provincia encontrada
     * @throws ResourceNotFoundException si no se encuentra una provincia con el ID especificado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinciaResponseDTO>> getProvinciaById(@PathVariable("id") Long id) {
        Optional<Provincia> provincia = provinciaService.findProvinciaById(id);
        return provincia.map(p -> {
            ProvinciaResponseDTO response = new ProvinciaResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getRegion() != null 
                    ? new ProvinciaResponseDTO.RegionSummaryDTO(p.getRegion().getId(), p.getRegion().getNombre())
                    : null
            );
            return ResponseEntity.ok(ApiResponse.success("Provincia recuperada exitosamente", response));
        }).orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada con ID: " + id));
    }

    /**
     * Obtiene todas las provincias que pertenecen a una región específica.
     * 
     * Este endpoint público permite recuperar todas las provincias asociadas
     * a una región particular utilizando el ID de la región. Es útil para
     * filtrar provincias por región geográfica.
     * 
     * @param regionId El identificador único de la región para filtrar provincias
     * @return ResponseEntity conteniendo ApiResponse con la lista de Provincia de la región especificada
     */
    @GetMapping("/region/{regionId}")
    public ResponseEntity<ApiResponse<List<ProvinciaResponseDTO>>> getProvinciasByRegionId(@PathVariable("regionId") Long regionId) {
        List<Provincia> provincias = provinciaService.findProvinciasByRegionId(regionId);
        List<ProvinciaResponseDTO> response = provincias.stream()
            .map(p -> new ProvinciaResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getRegion() != null 
                    ? new ProvinciaResponseDTO.RegionSummaryDTO(p.getRegion().getId(), p.getRegion().getNombre())
                    : null
            ))
            .collect(Collectors.toList());
        if (response.isEmpty()) {
            return ResponseEntity.ok(ApiResponse.success("No se encontraron provincias para la región con ID: " + regionId, response));
        }
        return ResponseEntity.ok(ApiResponse.success("Provincias para la región recuperadas exitosamente", response));
    }

    /**
     * Crea una nueva provincia en el sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados crear nuevas provincias.
     * La provincia debe incluir un nombre único y estar asociada a una región existente.
     * 
     * @param provincia La entidad Provincia a crear con los datos necesarios
     * @return ResponseEntity conteniendo ApiResponse con la Provincia creada
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Provincia>> createProvincia(@RequestBody Provincia provincia) {
        Provincia newProvincia = provinciaService.saveProvincia(provincia);
        return new ResponseEntity<>(ApiResponse.success("Provincia creada exitosamente", newProvincia), HttpStatus.CREATED);
    }

    /**
     * Actualiza una provincia existente.
     * 
     * Este endpoint protegido permite a usuarios autenticados modificar
     * los datos de una provincia existente. El ID de la provincia a actualizar
     * se especifica en la URL y debe coincidir con una provincia existente.
     * 
     * @param id El identificador único de la provincia a actualizar
     * @param provincia La entidad Provincia con los datos actualizados
     * @return ResponseEntity conteniendo ApiResponse con la Provincia actualizada
     * @throws ResourceNotFoundException si no se encuentra una provincia con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Provincia>> updateProvincia(@PathVariable("id") Long id, @RequestBody Provincia provincia) {
        return provinciaService.findProvinciaById(id)
                .map(existingProvincia -> {
                    provincia.setId(id);
                    Provincia updatedProvincia = provinciaService.saveProvincia(provincia);
                    return ResponseEntity.ok(ApiResponse.success("Provincia actualizada exitosamente", updatedProvincia));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Provincia no encontrada con ID: " + id));
    }

    /**
     * Elimina una provincia del sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados eliminar
     * una provincia específica del sistema utilizando su ID. La eliminación
     * es permanente y no se puede deshacer. Se debe tener cuidado de no
     * eliminar provincias que estén siendo utilizadas por platos existentes.
     * 
     * @param id El identificador único de la provincia a eliminar
     * @return ResponseEntity conteniendo ApiResponse con mensaje de confirmación
     * @throws ResourceNotFoundException si no se encuentra una provincia con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvincia(@PathVariable("id") Long id) {
        if (provinciaService.findProvinciaById(id).isPresent()) {
            provinciaService.deleteProvincia(id);
            return ResponseEntity.ok(ApiResponse.success("Provincia eliminada exitosamente"));
        }
        throw new ResourceNotFoundException("Provincia no encontrada con ID: " + id);
    }
}