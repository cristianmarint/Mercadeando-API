package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.Moneda;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cristianmarint
 * @Date 2021-01-14 11:54
 */
@JsonPropertyOrder({
        "id",
        "self",
        "currency",
        "total",
        "metodo",
        "orden"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponse {
    private Long id;
    private Link self;
    private Moneda moneda;
    private String total;
    private PagoMetodo metodo;
    private String fecha;
    private Link orden;
}
