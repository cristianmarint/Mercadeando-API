package com.api.mercadeando.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-22 11:15
 */
@JsonPropertyOrder({
        "count",
        "categorias",
        "links"
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriasResponse {
    private int count = 0;
    private List<CategoriaResponse> categorias = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
}
