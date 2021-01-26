package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.OrdenData;
import com.api.mercadeando.domain.data.PagoData;
import com.api.mercadeando.domain.dto.OrdenResponse;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.dto.PagoResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_ORDENES_V1;
import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PAGOS_V1;

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
                    request.setOrdenEstado(OrdenEstado.PAGADO);
                    request.setTotal(String.valueOf(orden.getTotal()));
                    pago = pagoData.addPago(request);
                    ordenData.addPagoId(orden.getId(), pago.getId());
                break;

            case CHECK:
            case TARJETA_CREDITO:
                    request.setOrdenEstado(OrdenEstado.PENDIENTE);
                    request.setTotal(String.valueOf(orden.getTotal()));
                    pago = pagoData.addPago(request);
                    ordenData.addPagoId(orden.getId(), pago.getId());
                break;

            case PAYPAL:
                    Map<String, Object> response = new HashMap<String, Object>();
                    Amount amount = new Amount();
                    amount.setCurrency(request.getMoneda().toString());
                    amount.setTotal(request.getTotal());
                    Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    List<Transaction> transactions = new ArrayList<Transaction>();
                    transactions.add(transaction);

                    Payer payer = new Payer();
                    payer.setPaymentMethod("paypal");

                    Payment payment = new Payment();
                    payment.setIntent("sale");
                    payment.setPayer(payer);
                    payment.setTransactions(transactions);

                    RedirectUrls redirectUrls = new RedirectUrls();
                    redirectUrls.setCancelUrl(URL_PAGOS_V1+"/cancelar");
                    redirectUrls.setReturnUrl(URL_ORDENES_V1+"/"+request.getOrdenId());
                    payment.setRedirectUrls(redirectUrls);
                    Payment createdPayment;
                    try {
                        String redirectUrl = "";
                        createdPayment = payment.create(context);
                        log.info("getRequestId -> "+ context.getRequestId());
                        log.info("getAccessToken -> "+ context.getAccessToken());
                        log.info("payment -> "+ payment);
                        if(createdPayment!=null){
                            List<Links> links = createdPayment.getLinks();
                            for (Links link:links) {
                                if(link.getRel().equals("approval_url")){
                                    redirectUrl = link.getHref();
                                    break;
                                }
                            }
                            pagoData.addPago(request);
                            response.put("status", "success");
                            response.put("redirect_url", redirectUrl);
                            log.info("response -> "+ response);
                            log.info("links -> "+ links);
                        }
                    } catch (PayPalRESTException e) {
                        log.error("Error happened during payment creation!");
                    }
                break;
            default:
                throw new BadRequestException("El metodo de pago no es valido: " + request.getMetodo());
        }
        return pago;
    }

    @PreAuthorize("hasAuthority('READ_PAGO')")
    public PagoResponse readPago(Long pagoId) throws BadRequestException, ResourceNotFoundException {
        if (pagoId==null) throw new BadRequestException("PagoId no puede ser Null");
        return pagoData.readCliente(pagoId);
    }

    private void validarPagoRequest(PagoRequest request) throws BadRequestException {
        if (request.getMoneda()==null) throw new BadRequestException("Moneda no puede ser Null");
        if (request.getMetodo()==null) throw new BadRequestException("Metodo de Pago no puede ser Null");
        if (request.getOrdenId()==null) throw new BadRequestException("OrdenId no puede ser Null");
    }


    public Map<String, Object> completarPago(HttpServletRequest req){
        Map<String, Object> response = new HashMap();
        Payment payment = new Payment();
        payment.setId(req.getParameter("paymentId"));
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(req.getParameter("payerId"));
        try {
//            APIContext context = new APIContext(clientId, clientSecret);
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }
}
