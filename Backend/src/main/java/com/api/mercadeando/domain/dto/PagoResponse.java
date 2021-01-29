package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.Moneda;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
        "orden",
        "paypal_payment_id",
        "paypal_payer_id",
        "paypal_url"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponse {
    private Long id;
    private Link self;
    @JsonProperty(value = "paypal_payment_id")
    private String paypalPaymentId;
    @JsonProperty(value = "paypal_url")
    private Link paypalUrl;
    private Moneda moneda;
    private BigDecimal total;
    private PagoMetodo metodo;
    private String fecha;
    private Link orden;
}
