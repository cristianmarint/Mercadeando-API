package com.api.mercadeando.controller;

import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.service.OrdenService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.awt.*;

import static com.api.mercadeando.controller.Mappings.URL_ORDENES_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-14 9:39
 */
@RestController
@RequestMapping(URL_ORDENES_V1)
@AllArgsConstructor
public class OrdenController {
    private final OrdenService ordenService;



    @PreAuthorize("hasAuthority('READ_ORDEN')")
    @GetMapping(value = "/{ordenId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrdenResponse> getOrden(@PathVariable(value = "ordenId") @Min(1) Long ordenId){
        try{
            if (ordenId==null) throw new BadRequestException();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ordenService.getCliente(ordenId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }catch (ResourceNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}
