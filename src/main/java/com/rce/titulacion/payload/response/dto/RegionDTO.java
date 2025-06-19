package com.rce.titulacion.payload.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private Long id;
    private String nombre;

    // Método para convertir de la entidad Region a este DTO
    public static RegionDTO fromEntity(com.rce.titulacion.model.Region region) {
        if (region == null) {
            return null;
        }
        return new RegionDTO(region.getId(), region.getNombre());
    }
}