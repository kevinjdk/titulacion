package com.rce.titulacion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "provincias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY) // Por defecto es EAGER, pero LAZY es mejor para rendimiento
    @JoinColumn(name = "region_id", nullable = false)
    @JsonBackReference
    private Region region;
}