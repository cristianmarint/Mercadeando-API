package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.Cliente;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cristianmarint
 * @Date 2021-01-16 7:57
 */

/**
 * API request para cuando se crea un solo Cliente.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenRequest {
    @ApiModelProperty(hidden = true)
    private Long id;
    private Boolean activo;
    private OrdenEstado estado;
    private Instant fecha;
    @ApiModelProperty(hidden = true)
    private BigDecimal total;
    private List<ordenProductos> ordenDetalle = new ArrayList<>();
    private Long cliente_id;
    @ApiModelProperty(hidden = true)
    private Cliente cliente;

    public void addOrdenProducto(Long producto_id, Long orden_id, Integer cantidad, BigDecimal precio) {
        ordenDetalle.add(new ordenProductos(
                producto_id,
                orden_id,
                cantidad,
                precio
        ));
    }

    /**
     * Permite que la orden sea creada almacenando multiples
     * detalles en una orden
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonSerialize
    public static class ordenProductos {
        private Long producto_id;
        @ApiModelProperty(hidden = true)
        private Long orden_id;
        private Integer cantidad;
        @ApiModelProperty(hidden = true)
        private BigDecimal precio;
    }
}
