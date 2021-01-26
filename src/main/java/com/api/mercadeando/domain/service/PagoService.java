package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.PagoData;
import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.infrastructure.persistence.entity.OrdenEstado;
import com.api.mercadeando.infrastructure.persistence.entity.PagoMetodo;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
//    @Autowired
    private final APIContext context;

    private final PagoData pagoData;

    public Map<String, Object> crearPago(PagoRequest request){

        if (request.getMetodo().equals(PagoMetodo.PAYPAL)){
            Map<String, Object> response = new HashMap<String, Object>();
            Amount amount = new Amount();
                amount.setCurrency(request.getCurrency());
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
                    response.put("status", "success");
                    response.put("redirect_url", redirectUrl);
                    log.info("response -> "+ response);
                    log.info("links -> "+ links);
                }
            } catch (PayPalRESTException e) {
                log.error("Error happened during payment creation!");
            }
        }

        if (request.getMetodo().equals(PagoMetodo.EFECTIVO)){
            request.setOrdenEstado(OrdenEstado.PAGADO);
        }

        if (request.getMetodo().equals(PagoMetodo.CHECK)){
            request.setOrdenEstado(OrdenEstado.PENDIENTE);
        }

        if (request.getMetodo().equals(PagoMetodo.TARJETA_CREDITO)){
            request.setOrdenEstado(OrdenEstado.PENDIENTE;
        }

        if (request.getMetodo().equals(PagoMetodo.TARJETA_DEBITO)){

        }

        if (request.getMetodo().equals(PagoMetodo.PAYPAL)){

        }

        return response;
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
