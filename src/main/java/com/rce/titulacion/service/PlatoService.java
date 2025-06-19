package com.rce.titulacion.service;

import com.rce.titulacion.model.Plato;
import com.rce.titulacion.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatoService {

    @Autowired
    private PlatoRepository platoRepository;

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
}