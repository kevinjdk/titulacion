package com.rce.titulacion.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePlatoRequest {
    private String nombre;
    private String descripcion;
    private String ingredientes;
    private List<String> preparacion;
    private Integer porciones;
    private String imageUrl;
    private Long categoriaId;
    private Long regionId;
    private Long provinciaId;
}
