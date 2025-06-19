package com.rce.titulacion.service;

import com.rce.titulacion.model.Region;
import com.rce.titulacion.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> findAllRegiones() {
        return regionRepository.findAll();
    }

    public Optional<Region> findRegionById(Long id) {
        return regionRepository.findById(id);
    }

    public Region saveRegion(Region region) {
        return regionRepository.save(region);
    }

    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }
}