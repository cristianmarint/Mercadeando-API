package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.PagoRequest;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.PagoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_PAGOS_V1;

@RestController
@RequestMapping(value = URL_PAGOS_V1)
@AllArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("hasAuthority('ADD_PAGO')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPago(@RequestBody() PagoRequest request){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.addPago(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('ADD_PAGO')")
    @GetMapping(value = "/{pagoId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity readPago(@PathVariable("pagoId") Long pagoId){
        try{
            if(pagoId == null) throw new BadRequestException("PagoId no puede ser null");
            return ResponseEntity.ok().body(pagoService.readPago(pagoId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}