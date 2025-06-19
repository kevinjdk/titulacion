package com.rce.titulacion.controller;

import com.rce.titulacion.model.Categoria;
import com.rce.titulacion.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Consulta pública
    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategorias() {
        List<Categoria> categorias = categoriaService.findAllCategorias();
        return ResponseEntity.ok(categorias);
    }

    // Consulta pública por ID
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getCategoriaById(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.findCategoriaById(id);
        return categoria.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) {
        Categoria newCategoria = categoriaService.saveCategoria(categoria);
        return new ResponseEntity<>(newCategoria, HttpStatus.CREATED);
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria categoria) {
        return categoriaService.findCategoriaById(id)
                .map(existingCategoria -> {
                    categoria.setId(id);
                    Categoria updatedCategoria = categoriaService.saveCategoria(categoria);
                    return ResponseEntity.ok(updatedCategoria);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Solo para administrador
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaService.findCategoriaById(id).isPresent()) {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}