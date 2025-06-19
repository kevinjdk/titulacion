package com.rce.titulacion.payload.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciaDTO {
    private Long id;
    private String nombre;
    private RegionDTO region; // Puedes incluir la región si es necesario

    // Método para convertir de la entidad Provincia a este DTO
    public static ProvinciaDTO fromEntity(com.rce.titulacion.model.Provincia provincia) {
        if (provincia == null) {
            return null;
        }
        // Llamada recursiva al DTO de Region
        return new ProvinciaDTO(provincia.getId(), provincia.getNombre(), RegionDTO.fromEntity(provincia.getRegion()));
    }
}