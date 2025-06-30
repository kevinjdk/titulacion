package com.rce.titulacion.payload.response.dto;

import com.rce.titulacion.model.Plato;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String ingredientes;
    private List<String> preparacion;
    private String imageUrl;
    private CategoriaDTO categoria;
    private ProvinciaDTO provincia;
    private RegionDTO region;

    public static PlatoResponseDTO fromEntity(Plato plato) {
        if (plato == null) {
            return null;
        }

        return new PlatoResponseDTO(
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                plato.getIngredientes(),
                plato.getPreparacion(),
                plato.getImageUrl(),
                new CategoriaDTO(plato.getCategoria().getId(), plato.getCategoria().getNombre()),
                new ProvinciaDTO(plato.getProvincia().getId(), plato.getProvincia().getNombre()),
                new RegionDTO(plato.getRegion().getId(), plato.getRegion().getNombre())
        );
    }
}