package com.api.mercadeando.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-19 10:00
 */

/**
 * API response para cuando se solicitan multiples productos.
 */
@JsonPropertyOrder({
        "count",
        "productos",
        "links"
})
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductosResponse {
    List<ProductoResponse> productos = new ArrayList<>();
    private int count = 0;
    private List<Link> links = new ArrayList<>();
}
