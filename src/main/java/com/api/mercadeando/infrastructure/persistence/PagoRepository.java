package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.PagoData;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.Pago;
import com.api.mercadeando.infrastructure.persistence.jpa.PagoJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.PagoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author cristianmarint
 * @Date 2021-01-25 9:10
 */
@Repository
public class PagoRepository implements PagoData {
    @Autowired
    private PagoJPARepository pagoJPARepository;
    @Autowired
    private PagoMapper pagoMapper;
    @Override
    public PagoResponse addPago(PagoRequest request) throws BadRequestException {
        if (request==null) throw new BadRequestException("PagoRequest no puede ser Null");
        Pago pago = pagoJPARepository.save(pagoMapper.mapOrdenRequestToOrden(request,null));
        return pagoMapper.mapPagoToPagoResponse(pago);
    }

    @Override
    public PagoResponse readCliente(Long pagoId) throws BadRequestException, ResourceNotFoundException {
        if (pagoId==null) throw new BadRequestException("PagoId no puede ser Null");
        Pago pago = pagoJPARepository.findById(pagoId).orElseThrow(() -> new ResourceNotFoundException(pagoId, "Pago"));
        return pagoMapper.mapPagoToPagoResponse(pago);
    }
}
