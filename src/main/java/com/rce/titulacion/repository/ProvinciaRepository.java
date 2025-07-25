package com.rce.titulacion.repository;

import com.rce.titulacion.model.Provincia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {
    List<Provincia> findByRegionId(Long regionId);
    
    @Query("SELECT p FROM Provincia p LEFT JOIN FETCH p.region")
    List<Provincia> findAllWithRegion();
    
    @Query("SELECT p FROM Provincia p LEFT JOIN FETCH p.region WHERE p.id = :id")
    Optional<Provincia> findByIdWithRegion(Long id);
}