package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.PagoData;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.infrastructure.persistence.entity.Orden;
import com.api.mercadeando.infrastructure.persistence.entity.Pago;
import com.api.mercadeando.infrastructure.persistence.jpa.OrdenJPARepository;
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
    private OrdenJPARepository ordenJPARepository;
    @Autowired
    private PagoMapper pagoMapper;

    @Autowired
    private AuthService authService;

    @Override
    public PagoResponse addPago(PagoRequest request) throws BadRequestException, ResourceNotFoundException {
        if (request==null) throw new BadRequestException("PagoRequest no puede ser Null");
        Pago pago=new Pago();
        pago.setUser(authService.getCurrentUser());

        Orden orden = ordenJPARepository.findById(request.getOrdenId()).orElseThrow(()-> new ResourceNotFoundException(request.getOrdenId(), "Orden"));
        request.setOrdenId(orden.getId());

        Pago save = pagoJPARepository.save(pagoMapper.mapPagoRequestToPago(request,pago));
        return pagoMapper.mapPagoToPagoResponse(save);
    }

    @Override
    public PagoResponse readPago(Long pagoId) throws BadRequestException, ResourceNotFoundException {
        if (pagoId==null) throw new BadRequestException("PagoId no puede ser Null");
        Pago pago = pagoJPARepository.findById(pagoId).orElseThrow(() -> new ResourceNotFoundException(pagoId, "Pago"));
        return pagoMapper.mapPagoToPagoResponse(pago);
    }

    @Override
    public PagoResponse findPagoByPaymentId(String paymentId) throws ResourceNotFoundException {
        return pagoMapper.mapPagoToPagoResponse(pagoJPARepository.findByPaymentId(paymentId).orElseThrow(()-> new ResourceNotFoundException("Pago no encontrado con payment id " + paymentId)));
    }
}
