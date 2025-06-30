package com.rce.titulacion.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "platos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    @ManyToOne
    @JoinColumn(name = "provincia_id", nullable = false)
    private Provincia provincia;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String ingredientes;

    @ElementCollection(fetch = FetchType.EAGER) // EAGER para que los pasos siempre se carguen con el plato
    @CollectionTable(name = "plato_preparacion_pasos", joinColumns = @JoinColumn(name = "plato_id")) // Define la tabla intermedia
    @OrderColumn // Mantiene el orden de la lista
    @Column(name = "paso", nullable = false, columnDefinition = "TEXT") // Define la columna para los pasos
    private List<String> preparacion;

    private String imageUrl;
}