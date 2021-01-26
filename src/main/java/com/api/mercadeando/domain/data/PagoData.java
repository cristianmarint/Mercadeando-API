package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;

/**
 * @author cristianmarint
 * @Date 2021-01-25 9:16
 */
public interface PagoData {
    PagoResponse addPago(PagoRequest request) throws BadRequestException;

    PagoResponse readCliente(Long pagoId) throws BadRequestException, ResourceNotFoundException;
}
