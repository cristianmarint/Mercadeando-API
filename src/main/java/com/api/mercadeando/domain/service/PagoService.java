package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.OrdenData;
import com.api.mercadeando.domain.data.PagoData;
import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.domain.dto.OrdenResponse;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.Moneda;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.api.mercadeando.infrastructure.controller.Mappings.*;

/**
 * @author cristianmarint
 * @Date 2021-01-24 9:11
 */
@Component
@AllArgsConstructor
@Slf4j
public class PagoService {
    @Autowired
    private final APIContext context;

    @Autowired
    private final PagoData pagoData;

    @Autowired
    private final OrdenData ordenData;

    @Autowired
    private final OrdenService ordenService;

    @PreAuthorize("hasAuthority('ADD_PAGO')")
    public PagoResponse addPago(PagoRequest request) throws BadRequestException, ResourceNotFoundException {
        validarPagoRequest(request);
        OrdenResponse orden;
        PagoResponse pago = null;
        try {
            orden = ordenData.readOrden(request.getOrdenId());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Orden no encontrada");
        }

        switch (request.getMetodo()){
            case EFECTIVO:
            case TARJETA_DEBITO:
                log.info("efectivo o tar. debito");
                    request.setOrdenEstado(OrdenEstado.PAGADO);
                    request.setTotal(orden.getTotal());
                    pago = pagoData.addPago(request);
                    this.completarPagoAddPago(orden.getId(),pago.getId());
                    ordenData.addPagoId(orden.getId(), pago.getId());
                break;

            case CHECK:
            case TARJETA_CREDITO:
                log.info("checkeo tar. credito");
                    request.setOrdenEstado(OrdenEstado.PENDIENTE);
                    request.setTotal(orden.getTotal());
                    pago = pagoData.addPago(request);
                    this.completarPagoAddPago(orden.getId(),pago.getId());
                    ordenData.addPagoId(orden.getId(), pago.getId());
                break;

            case PAYPAL:
                log.info("paypal");
                int intercambio;
                if (request.getMoneda().equals(Moneda.EUR)) {
                        intercambio = 4405;
                    } else if (request.getMoneda().equals(Moneda.USD)) {
                        intercambio = 3640;
                }else {
                    throw new BadRequestException("Moneda invalida, use USD o EUR para el metodo de pago PayPal");
                }

                Map<String, Object> response = new HashMap<>();
                Amount amount = new Amount();
                    amount.setCurrency(request.getMoneda().name());
                    amount.setTotal(orden.getTotal().divide(BigDecimal.valueOf(intercambio),2, RoundingMode.HALF_UP).toPlainString());

                Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    List<Transaction> transactions = new ArrayList<>();
                    transactions.add(transaction);

                Payer payer = new Payer();
                    payer.setPaymentMethod("paypal");

                Payment payment = new Payment();
                    payment.setIntent("sale");
                    payment.setPayer(payer);
                    payment.setTransactions(transactions);

                RedirectUrls redirectUrls = new RedirectUrls();
                redirectUrls.setCancelUrl(FULL_BASE_V1+URL_PAGOS_V1+"/cancelar");
                redirectUrls.setReturnUrl(FULL_BASE_V1+URL_PAGOS_V1+"/confirmar");
                payment.setRedirectUrls(redirectUrls);
                log.info(String.valueOf(payment));
                Payment createdPayment;

                try {
                    String redirectUrl = "";
                    createdPayment = payment.create(context);

                    if(createdPayment!=null){
                        List<Links> links = createdPayment.getLinks();
                        for (Links link:links) {
                            if(link.getRel().equals("approval_url")){
                                redirectUrl = link.getHref();
                                break;
                            }
                        }
                        response.put("status", "success");
                        response.put("redirect_url", redirectUrl);

                        request.setOrdenEstado(OrdenEstado.PENDIENTE);
                            request.setTotal(orden.getTotal());
                            request.setPaypalPaymentId(createdPayment.getId());
                        pago = pagoData.addPago(request);
                        ordenData.addPagoId(orden.getId(), pago.getId());
                        pago.setPaypalUrl(new Link("self",redirectUrl));
                    }
                } catch (PayPalRESTException e) {
                    throw new MercadeandoException("Error creado el pago");
                }
                break;
            default:
                throw new BadRequestException("El metodo de pago no es valido: " + request.getMetodo());
        }
        return pago;
    }

    public void completarPagoAddPago(Long ordenId, Long pagoId) throws BadRequestException, ResourceNotFoundException {
        PagoResponse pago = pagoData.readPago(pagoId);
        OrdenResponse orden = ordenData.readOrden(ordenId);
        if (!orden.getEstado().equals(OrdenEstado.PAGADO)) throw new MercadeandoException("La orden no ha sido pagada");
        if (!orden.getPago().getMetodo().equals(PagoMetodo.PAYPAL)) {
            ordenData.completarOrden(ordenId);
        }
    }

    public void completarPagoPayPal(String paymentId) throws ResourceNotFoundException {
        PagoResponse pago = pagoData.findPagoByPaymentId(paymentId);
        if (pago.getId()==null) return  ;

    }

    @PreAuthorize("hasAuthority('READ_PAGO')")
    public PagoResponse readPago(Long pagoId) throws BadRequestException, ResourceNotFoundException {
        if (pagoId==null) throw new BadRequestException("PagoId no puede ser Null");
        return pagoData.readPago(pagoId);
    }

    private void validarPagoRequest(PagoRequest request) throws BadRequestException {
        if (request.getMoneda()==null) throw new BadRequestException("Moneda no puede ser Null");
        if (request.getMetodo()==null) throw new BadRequestException("Metodo de Pago no puede ser Null");
        if (request.getOrdenId()==null) throw new BadRequestException("OrdenId no puede ser Null");
    }


    public Payment confirmarPago(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(context, paymentExecute);
    }
}
