package com.api.mercadeando.infrastructure.persistence.mapper;

import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.infrastructure.persistence.entity.Pago;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static com.api.mercadeando.domain.utils.formatDates.instantToString;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_ORDENES_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PAGOS_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-26 8:59
 */
@Component
@AllArgsConstructor
public class PagoMapper {
    private final AuthService authService;
    public Pago mapOrdenRequestToOrden(PagoRequest request, Pago pago) throws BadRequestException {
        if (request==null) throw new BadRequestException("PagoRequest no puede ser Null");
        if (pago==null) pago = new Pago();

        if (request.getId()!=null) pago.setId(request.getId());
        if (request.getFecha()!=null) {
            pago.setFecha(request.getFecha());
        }else {
            pago.setFecha(Instant.now());
        }
        if (request.getMoneda()!=null) pago.setMoneda(request.getMoneda());
        if (request.getTotal()!=null) pago.setTotal(request.getTotal());
        if (request.getMetodo()!=null) pago.setMetodo(request.getMetodo());

        pago.setUser(authService.getCurrentUser());
        return pago;
    }

    public PagoResponse mapPagoToPagoResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        if (pago.getId()!=null) {
            response.setId(pago.getId());
            response.setSelf(new Link("self",URL_PAGOS_V1+"/"+pago.getId()));
        }
        if (pago.getFecha()!=null) response.setFecha(instantToString(pago.getFecha()));
        if (pago.getMoneda()!=null) response.setMoneda(pago.getMoneda());
        if (pago.getTotal()!=null) response.setTotal(pago.getTotal());
        if (pago.getMetodo()!=null) response.setMetodo(pago.getMetodo());
        if (pago.getOrden()!=null) {
            response.setOrden(new Link("orden",URL_ORDENES_V1+"/"+pago.getOrden().getId()));
            response.setMetodo(pago.getMetodo());
        }
        return response;
    }
}
