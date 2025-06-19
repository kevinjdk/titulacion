package com.rce.titulacion.payload.response.dto;

import com.rce.titulacion.model.Plato;

import lombok.Data;

@Data
public class PlatoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ingredientes;
    private String preparacion;
    private String imageUrl;

    // DTOs para las relaciones, que solo incluyen los campos que te interesan
    private Long regionId;
    private String regionNombre;
    private Long provinciaId;
    private String provinciaNombre;
    private Long categoriaId;
    private String categoriaNombre;

    // Constructor o métodos de mapeo para convertir de entidad a DTO
    public static PlatoResponseDTO fromEntity(Plato plato) {
        PlatoResponseDTO dto = new PlatoResponseDTO();
        dto.setId(plato.getId());
        dto.setNombre(plato.getNombre());
        dto.setDescripcion(plato.getDescripcion());
        dto.setIngredientes(plato.getIngredientes());
        dto.setPreparacion(plato.getPreparacion());
        dto.setImageUrl(plato.getImageUrl());

        // Asegúrate de que las entidades relacionadas no sean null antes de acceder a ellas
        if (plato.getRegion() != null) {
            dto.setRegionId(plato.getRegion().getId());
            dto.setRegionNombre(plato.getRegion().getNombre());
        }
        if (plato.getProvincia() != null) {
            dto.setProvinciaId(plato.getProvincia().getId());
            dto.setProvinciaNombre(plato.getProvincia().getNombre());
        }
        if (plato.getCategoria() != null) {
            dto.setCategoriaId(plato.getCategoria().getId());
            dto.setCategoriaNombre(plato.getCategoria().getNombre());
        }
        return dto;
    }
}