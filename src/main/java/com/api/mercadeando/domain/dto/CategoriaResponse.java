package com.api.mercadeando.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:12
 */
@JsonPropertyOrder({
        "id",
        "self",
        "nombre",
        "descripcion",
        "productos"
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaResponse {
    private Long id;
    private Link self;
    private String nombre;
    private String descripcion;
    private List<Link> productos;
}
