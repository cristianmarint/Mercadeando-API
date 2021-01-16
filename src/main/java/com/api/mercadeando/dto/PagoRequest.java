package com.api.mercadeando.dto;

import com.api.mercadeando.entity.PagoMetodo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * @author cristianmarint
 * @Date 2021-01-16 9:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequest {
    private PagoMetodo metodo;
    private Instant fecha;
}
