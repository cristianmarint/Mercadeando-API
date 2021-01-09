package com.api.mercadeando.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * API response para cuando se solicitas multiples Clientes.
 */
@JsonPropertyOrder({
        "count",
        "clientes"
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientesResponse {
    private int count = 0;
    private List<ClienteResponse> clientes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
}
