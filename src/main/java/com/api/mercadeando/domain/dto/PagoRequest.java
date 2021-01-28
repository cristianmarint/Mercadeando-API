package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.Moneda;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * @author cristianmarint
 * @Date 2021-01-16 9:14
 */
@JsonPropertyOrder({
        "orden-id",
        "moneda",
        "metodo"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequest {
    @ApiModelProperty(hidden = true)
    private Long id;
    @NotBlank
    @JsonProperty(required = true)
    private Moneda moneda;
    @ApiModelProperty(hidden = true)
    private @NotBlank BigDecimal total;
    @NotBlank
    @JsonProperty(required = true)
    private PagoMetodo metodo;
    @ApiModelProperty(hidden = true)
    @NotBlank
    private Instant fecha;

    private String paypalPaymentId;

    @NotBlank
    @JsonProperty(required = true, value = "orden-id")
    private Long ordenId;
    @ApiModelProperty(hidden = true)
    @NotBlank
    private OrdenEstado ordenEstado;
}
