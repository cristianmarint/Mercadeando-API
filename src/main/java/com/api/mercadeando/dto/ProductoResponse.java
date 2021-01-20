package com.api.mercadeando.dto;

import com.api.mercadeando.entity.FileStorage;
import com.api.mercadeando.entity.ProductoEstado;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

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
        "estado",
        "fotos"
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
    private Set<UploadFileResponse> fotos = new HashSet<>();
    private ProductoEstado estado;
}
