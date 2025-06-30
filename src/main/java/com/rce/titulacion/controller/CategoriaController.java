package com.rce.titulacion.controller;

import com.rce.titulacion.model.Categoria;
import com.rce.titulacion.exception.ResourceNotFoundException;
import com.rce.titulacion.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.rce.titulacion.payload.response.ApiResponse;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de categorías de platos.
 * 
 * Este controlador proporciona endpoints para realizar operaciones CRUD
 * sobre las entidades Categoria, incluyendo consultas públicas para
 * obtener información de categorías y operaciones administrativas
 * que requieren autenticación para crear, actualizar y eliminar categorías.
 * 
 * @author Kevin
 * @version 1.0
 * @since 2025
 */
@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    /**
     * Obtiene todas las categorías disponibles.
     * 
     * Este endpoint público devuelve una lista completa de todas las categorías
     * registradas en el sistema. Las categorías son utilizadas para clasificar
     * los diferentes tipos de platos.
     * 
     * @return ResponseEntity conteniendo ApiResponse con la lista de Categoria
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Categoria>>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAllCategorias();
        return ResponseEntity.ok(ApiResponse.success("Categorías recuperadas exitosamente", categorias));
    }

    /**
     * Obtiene una categoría específica por su ID.
     * 
     * Este endpoint público permite recuperar los detalles de una categoría
     * utilizando su identificador único. Si la categoría no existe, se lanza
     * una excepción ResourceNotFoundException.
     * 
     * @param id El identificador único de la categoría a buscar
     * @return ResponseEntity conteniendo ApiResponse con la Categoria encontrada
     * @throws ResourceNotFoundException si no se encuentra una categoría con el ID especificado
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findCategoriaById(id);
        return categoria.map(c -> ResponseEntity.ok(ApiResponse.success("Categoría recuperada exitosamente", c)))
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    /**
     * Crea una nueva categoría en el sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados crear nuevas categorías
     * para clasificar platos. La categoría debe incluir un nombre único y descriptivo.
     * 
     * @param categoria La entidad Categoria a crear con los datos necesarios
     * @return ResponseEntity conteniendo ApiResponse con la Categoria creada
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> createCategoria(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaService.saveCategoria(categoria);
        return new ResponseEntity<>(ApiResponse.success("Categoría creada exitosamente", newCategoria), HttpStatus.CREATED);
    }

    /**
     * Actualiza una categoría existente.
     * 
     * Este endpoint protegido permite a usuarios autenticados modificar
     * los datos de una categoría existente. El ID de la categoría a actualizar
     * se especifica en la URL y debe coincidir con una categoría existente.
     * 
     * @param id El identificador único de la categoría a actualizar
     * @param categoria La entidad Categoria con los datos actualizados
     * @return ResponseEntity conteniendo ApiResponse con la Categoria actualizada
     * @throws ResourceNotFoundException si no se encuentra una categoría con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.findCategoriaById(id)
                .map(existingCategoria -> {
                    categoria.setId(id);
                    Categoria updatedCategoria = categoriaService.saveCategoria(categoria);
                    return ResponseEntity.ok(ApiResponse.success("Categoría actualizada exitosamente", updatedCategoria));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    /**
     * Elimina una categoría del sistema.
     * 
     * Este endpoint protegido permite a usuarios autenticados eliminar
     * una categoría específica del sistema utilizando su ID. La eliminación
     * es permanente y no se puede deshacer. Se debe tener cuidado de no
     * eliminar categorías que estén siendo utilizadas por platos existentes.
     * 
     * @param id El identificador único de la categoría a eliminar
     * @return ResponseEntity conteniendo ApiResponse con mensaje de confirmación
     * @throws ResourceNotFoundException si no se encuentra una categoría con el ID especificado
     * @throws org.springframework.security.access.AccessDeniedException si el usuario no está autenticado
     */
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategoria(@PathVariable Long id) {
        if (categoriaService.findCategoriaById(id).isPresent()) {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.ok(ApiResponse.success("Categoría eliminada exitosamente"));
        }
        throw new ResourceNotFoundException("Categoría no encontrada con ID: " + id);
    }
}