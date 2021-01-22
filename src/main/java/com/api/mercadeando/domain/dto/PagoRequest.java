package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
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
