package com.api.mercadeando.dto;

import com.api.mercadeando.entity.PagoMetodo;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cristianmarint
 * @Date 2021-01-14 11:54
 */
@JsonPropertyOrder({
        "fecha",
        "metodo"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponse {
    private String metodo;
    private String fecha;
}
