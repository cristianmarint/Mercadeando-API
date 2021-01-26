package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.service.PagoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PAGOS_V1;

@RestController
@RequestMapping(value = URL_PAGOS_V1)
@AllArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "make/payment")
    public Map<String, Object> crearPago(@RequestBody() PagoRequest request){
        return pagoService.crearPago(request);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping(value = "complete/payment")
    public Map<String, Object> completarPago(HttpServletRequest request, @RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
        return pagoService.completarPago(request);
    }


}