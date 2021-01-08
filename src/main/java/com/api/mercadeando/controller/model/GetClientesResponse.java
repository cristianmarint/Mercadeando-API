package com.api.mercadeando.controller.model;

import com.api.mercadeando.HATEOS.Link;
import com.api.mercadeando.entity.Cliente;
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
public class GetClientesResponse {
    private int count = 0;
    private List<GetClienteResponse> clientes = new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    /**
     * Transforma una lista de Cliente Entities a ClienteResponse para ser retornado por la API al frontend
     * @param clientes
     * @param offset
     * @param limit
     * @return
     */
    public static GetClientesResponse from(List<Cliente> clientes, int offset, int limit){
        GetClientesResponse response = new GetClientesResponse();
        if (clientes!=null || !clientes.isEmpty()){
            response.setCount(clientes.size());
            clientes.forEach(cliente -> response.getClientes().add(GetClienteResponse.from(cliente)));
            if (offset>0){
                response.getLinks().add(
                        new Link("prev",
                                String.format("/api/v1/clientes?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format("/api/v1/clientes?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }
}
