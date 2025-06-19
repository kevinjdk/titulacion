package com.rce.titulacion.repository;

import com.rce.titulacion.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    List<Provincia> findByRegionId(Long regionId);
}