package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.OrdenRequest;
import com.api.mercadeando.domain.dto.OrdenResponse;
import com.api.mercadeando.domain.dto.OrdenesResponse;
import com.api.mercadeando.domain.dto.ProductoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;

/**
 * @author cristianmarint
 * @Date 2021-01-21 3:14
 */
public interface OrdenData {
    OrdenResponse readOrden(Long ordenId) throws ResourceNotFoundException, BadRequestException;

    OrdenesResponse readOrdenes(int offset, int limit);

    OrdenResponse addOrden(OrdenRequest request) throws ResourceNotFoundException, BadRequestException;

    void editOrden(Long ordenId, PagoMetodo pagoMetodo) throws BadRequestException, ResourceNotFoundException;

    void softDeleteOrden(Long ordenId,Boolean estado) throws BadRequestException, ResourceNotFoundException;
}
