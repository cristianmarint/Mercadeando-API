package com.api.mercadeando.dto;

import com.api.mercadeando.entity.OrdenEstado;
import com.api.mercadeando.entity.Pago;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:43
 */
@JsonPropertyOrder({
        "id",
        "self",
        "activo",
        "fecha",
        "precio",
        "estado",
        "pago",
        "cliente",
        "productos"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenResponse {
    private Long id;
    private Link self;
    private Boolean activo;
    private OrdenEstado estado;
    private String fecha;
    private BigDecimal precio;
    private PagoResponse pago= new PagoResponse();

    private Link cliente;
    private Map<String, Link> productos;

}
