package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CLIENTES_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_ORDENES_V1;
/**
 * @author cristianmarint
 * @Date 2021-01-14 10:05
 */

/**
 * API Response para cuando se solicitan multiples ordenes
 */
@JsonPropertyOrder({
        "count",
        "ordenes",
        "links"
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesResponse {
    private int count=0;
    private List<OrdenesDetalle> ordenes=new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    /**
     * Permite vincular todos las ordenes con sus datos
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonSerialize
    public static class OrdenesDetalle{
        private Long id;
        private Link self;
        private Boolean activo;
        private OrdenEstado estado;
        private String fecha;
        private BigDecimal total;
        private PagoResponse pago=new PagoResponse();
        private Link cliente;
    }

    public void addOrdenesDetalle(Long ordenId,Boolean activo,OrdenEstado estado,String fecha,BigDecimal total,PagoResponse pago,Long clienteId){
        Link self = new Link("self", String.format(URL_ORDENES_V1 + "/%s",ordenId));
        Link cliente = new Link("cliente", String.format(URL_CLIENTES_V1 + "/%s",clienteId));
        ordenes.add(new OrdenesDetalle(
                ordenId,
                self,
                activo,
                estado,
                fecha,
                total,
                pago,
                cliente
        ));
    }
}
