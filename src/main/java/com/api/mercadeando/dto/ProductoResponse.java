package com.api.mercadeando.dto;

import com.api.mercadeando.entity.ProductoEstado;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * @author cristianmarint
 * @Date 2021-01-19 8:56
 */
/**
 * API response para cuando se solicita un Producto.
 */
@JsonPropertyOrder({
        "id",
        "self",
        "activo",
        "nombre",
        "descripcion",
        "peso",
        "unidades",
        "precio",
        "foto",
        "estado"
})
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ProductoResponse {
    private Long id;
    private Link self;
    private Boolean activo;
    private String nombre;
    private String descripcion;
    private Double peso;
    private Integer unidades;
    private BigDecimal precio;
    private String foto;
    private ProductoEstado estado;
}
