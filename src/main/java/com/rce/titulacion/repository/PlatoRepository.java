package com.rce.titulacion.repository;

import com.rce.titulacion.model.Plato;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlatoRepository extends JpaRepository<Plato, Long> {
    List<Plato> findByNombreContainingIgnoreCase(String nombre);
    List<Plato> findByCategoriaNombre(String categoriaNombre);
    List<Plato> findByRegionNombre(String regionNombre);
    List<Plato> findByProvinciaNombre(String provinciaNombre);
}