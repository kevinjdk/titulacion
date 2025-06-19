package com.rce.titulacion.service;

import com.rce.titulacion.model.Provincia;
import com.rce.titulacion.repository.ProvinciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinciaService {

    @Autowired
    private ProvinciaRepository provinciaRepository;

    public List<Provincia> findAllProvincias() {
        return provinciaRepository.findAll();
    }

    public Optional<Provincia> findProvinciaById(Long id) {
        return provinciaRepository.findById(id);
    }

    public Provincia saveProvincia(Provincia provincia) {
        return provinciaRepository.save(provincia);
    }

    public void deleteProvincia(Long id) {
        provinciaRepository.deleteById(id);
    }

    public List<Provincia> findProvinciasByRegionId(Long regionId) {
        return provinciaRepository.findByRegionId(regionId);
    }
}