package com.api.mercadeando.dto;

import com.api.mercadeando.entity.Orden;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-14 10:05
 */
@JsonPropertyOrder({
        "count",
        "ordenes"
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesResponse {
    private int count=0;
    private List<OrdenResponse> ordenes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
}
