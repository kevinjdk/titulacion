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

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Consulta pública
    @GetMapping // Ahora devuelve ApiResponse<List<Categoria>>
    public ResponseEntity<ApiResponse<List<Categoria>>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAllCategorias();
        return ResponseEntity.ok(ApiResponse.success("Categorías recuperadas exitosamente", categorias));
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Categoria>> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findCategoriaById(id);
        return categoria.map(c -> ResponseEntity.ok(ApiResponse.success("Categoría recuperada exitosamente", c)))
                        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ApiResponse<Categoria>> createCategoria(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaService.saveCategoria(categoria);
        return new ResponseEntity<>(ApiResponse.success("Categoría creada exitosamente", newCategoria), HttpStatus.CREATED);
    }

    // Solo para administrador
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

    // Solo para administrador
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