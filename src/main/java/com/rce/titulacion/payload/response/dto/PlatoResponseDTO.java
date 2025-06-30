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
    private List<String> preparacion; // Cambiado a List<String>
    private String imageUrl;
    private CategoriaDTO categoria;
    private ProvinciaDTO provincia;
    private RegionDTO region;

    /**
     * Método de fábrica estático para convertir una entidad Plato a un PlatoResponseDTO.
     * Este método se encarga de mapear los campos, incluyendo las entidades relacionadas,
     * a sus respectivos DTOs para una respuesta limpia al cliente.
     *
     * @param plato La entidad Plato a convertir.
     * @return Un nuevo objeto PlatoResponseDTO.
     */
    public static PlatoResponseDTO fromEntity(Plato plato) {
        if (plato == null) {
            return null;
        }

        return new PlatoResponseDTO(
                plato.getId(),
                plato.getNombre(),
                plato.getDescripcion(),
                plato.getIngredientes(),
                plato.getPreparacion(), // Se asigna directamente la lista
                plato.getImageUrl(),
                // Mapea las entidades anidadas a sus DTOs correspondientes
                new CategoriaDTO(plato.getCategoria().getId(), plato.getCategoria().getNombre()),
                new ProvinciaDTO(plato.getProvincia().getId(), plato.getProvincia().getNombre()),
                new RegionDTO(plato.getRegion().getId(), plato.getRegion().getNombre())
        );
    }
}