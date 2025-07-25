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

/**
 * Controlador REST para la gestión de regiones geográficas.
 * 
 * Este controlador proporciona endpoints para realizar operaciones CRUD
 * sobre las entidades Region, incluyendo consultas públicas para obtener
 * información de regiones y operaciones administrativas que requieren
 * autenticación para crear, actualizar y eliminar regiones.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/regiones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * Obtiene todas las regiones disponibles.
     * 
     * Este endpoint público devuelve una lista completa de todas las regiones
     * geográficas registradas en el sistema. Las regiones son utilizadas para
     * organizar geográficamente las provincias y clasificar los platos por origen.
     * 
     * @return ResponseEntity conteniendo ApiResponse con la lista de Region
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Region>>> getAllRegiones() {
        List<Region> regiones = regionService.findAllRegiones();
        return ResponseEntity.ok(ApiResponse.success("Regiones recuperadas exitosamente", regiones));
    }

    /**
     * Obtiene una región específica por su ID.
     * 
     * Este endpoint público permite recuperar los detalles de una región
     * utilizando su identificador único. Si la región no existe, se lanza
     * una excepción ResourceNotFoundException.
     * 
     * @param id El identificador único de la región a buscar
     * @return ResponseEntity conteniendo ApiResponse con la Region encontrada
     * @throws ResourceNotFoundException si no se encuentra una región con el ID especificado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Region>> getRegionById(@PathVariable("id") Long id) {
        Optional<Region> region = regionService.findRegionById(id);
        return region.map(r -> ResponseEntity.ok(ApiResponse.success("Región recuperada exitosamente", r)))
                     .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada con ID: " + id));
    }

    /**
     * Crea una nueva región en el sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados crear nuevas regiones
     * geográficas. La región debe incluir un nombre único y descriptivo que
     * identifique claramente el área geográfica.
     * 
     * @param region La entidad Region a crear con los datos necesarios
     * @return ResponseEntity conteniendo ApiResponse con la Region creada
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Region>> createRegion(@RequestBody Region region) {
        Region newRegion = regionService.saveRegion(region);
        return new ResponseEntity<>(ApiResponse.success("Región creada exitosamente", newRegion), HttpStatus.CREATED);
    }

    /**
     * Actualiza una región existente.
     * 
     * Este endpoint protegido permite a usuarios autenticados modificar
     * los datos de una región existente. El ID de la región a actualizar
     * se especifica en la URL y debe coincidir con una región existente.
     * 
     * @param id El identificador único de la región a actualizar
     * @param region La entidad Region con los datos actualizados
     * @return ResponseEntity conteniendo ApiResponse con la Region actualizada
     * @throws ResourceNotFoundException si no se encuentra una región con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Region>> updateRegion(@PathVariable("id") Long id, @RequestBody Region region) {
        return regionService.findRegionById(id)
                .map(existingRegion -> {
                    region.setId(id);
                    Region updatedRegion = regionService.saveRegion(region);
                    return ResponseEntity.ok(ApiResponse.success("Región actualizada exitosamente", updatedRegion));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada con ID: " + id));
    }

    /**
     * Elimina una región del sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados eliminar
     * una región específica del sistema utilizando su ID. La eliminación
     * es permanente y no se puede deshacer. Se debe tener cuidado de no
     * eliminar regiones que contengan provincias asociadas.
     * 
     * @param id El identificador único de la región a eliminar
     * @return ResponseEntity conteniendo ApiResponse con mensaje de confirmación
     * @throws ResourceNotFoundException si no se encuentra una región con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable("id") Long id) {
        if (regionService.findRegionById(id).isPresent()) {
            regionService.deleteRegion(id);
            return ResponseEntity.ok(ApiResponse.success("Región eliminada exitosamente"));
        }
        throw new ResourceNotFoundException("Región no encontrada con ID: " + id);
    }
}