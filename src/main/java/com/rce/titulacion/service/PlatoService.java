package com.rce.titulacion.service;

import com.rce.titulacion.model.Plato;
import com.rce.titulacion.model.Categoria;
import com.rce.titulacion.model.Region;
import com.rce.titulacion.model.Provincia;
import com.rce.titulacion.payload.request.CreatePlatoRequest;
import com.rce.titulacion.repository.PlatoRepository;
import com.rce.titulacion.repository.CategoriaRepository;
import com.rce.titulacion.repository.RegionRepository;
import com.rce.titulacion.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    @Autowired
    private PlatoRepository platoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private RegionRepository regionRepository;
    
    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Plato> findAllPlatos() {
        return platoRepository.findAll();
    }

    public Optional<Plato> findPlatoById(Long id) {
        return platoRepository.findById(id);
    }

    public Plato savePlato(Plato plato) {
        return platoRepository.save(plato);
    }

    public void deletePlato(Long id) {
        platoRepository.deleteById(id);
    }

    public List<Plato> searchPlatosByNombre(String nombre) {
        return platoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Plato> findPlatosByCategoria(String categoriaNombre) {
        return platoRepository.findByCategoriaNombre(categoriaNombre);
    }

    public List<Plato> findPlatosByRegion(String regionNombre) {
        return platoRepository.findByRegionNombre(regionNombre);
    }

    public List<Plato> findPlatosByProvincia(String provinciaNombre) {
        return platoRepository.findByProvinciaNombre(provinciaNombre);
    }

    public Plato createPlatoFromRequest(CreatePlatoRequest request) {
        // Buscar las entidades relacionadas por ID
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));
        
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Región no encontrada con ID: " + request.getRegionId()));
        
        Provincia provincia = provinciaRepository.findById(request.getProvinciaId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con ID: " + request.getProvinciaId()));

        // Crear el plato con las entidades relacionadas
        Plato plato = new Plato();
        plato.setNombre(request.getNombre());
        plato.setDescripcion(request.getDescripcion());
        plato.setIngredientes(request.getIngredientes());
        plato.setPreparacion(request.getPreparacion());
        plato.setPorciones(request.getPorciones());
        plato.setImageUrl(request.getImageUrl());
        plato.setCategoria(categoria);
        plato.setRegion(region);
        plato.setProvincia(provincia);

        return platoRepository.save(plato);
    }

    public Plato updatePlatoFromRequest(Long id, CreatePlatoRequest request) {
        // Buscar el plato existente
        Plato existingPlato = platoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));

        // Buscar las entidades relacionadas por ID
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));
        
        Region region = regionRepository.findById(request.getRegionId())
                .orElseThrow(() -> new RuntimeException("Región no encontrada con ID: " + request.getRegionId()));
        
        Provincia provincia = provinciaRepository.findById(request.getProvinciaId())
                .orElseThrow(() -> new RuntimeException("Provincia no encontrada con ID: " + request.getProvinciaId()));

        // Actualizar los datos del plato
        existingPlato.setNombre(request.getNombre());
        existingPlato.setDescripcion(request.getDescripcion());
        existingPlato.setIngredientes(request.getIngredientes());
        existingPlato.setPreparacion(request.getPreparacion());
        existingPlato.setPorciones(request.getPorciones());
        existingPlato.setImageUrl(request.getImageUrl());
        existingPlato.setCategoria(categoria);
        existingPlato.setRegion(region);
        existingPlato.setProvincia(provincia);

        return platoRepository.save(existingPlato);
    }
}