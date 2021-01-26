package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.PagoRequest;

/**
 * @author cristianmarint
 * @Date 2021-01-25 9:16
 */
public interface PagoData {
    void addPago(PagoRequest request);
}
