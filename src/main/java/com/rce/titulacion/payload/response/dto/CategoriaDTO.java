package com.rce.titulacion.payload.response.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {
    private Long id;
    private String nombre;

    // Método para convertir de la entidad Categoria a este DTO
    public static CategoriaDTO fromEntity(com.rce.titulacion.model.Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }
}