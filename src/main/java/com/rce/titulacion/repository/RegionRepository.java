package com.rce.titulacion.repository;

import com.rce.titulacion.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}