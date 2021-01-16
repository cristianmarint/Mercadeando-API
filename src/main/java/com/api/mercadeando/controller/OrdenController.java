package com.api.mercadeando.controller;

import com.api.mercadeando.dto.OrdenRequest;
import com.api.mercadeando.dto.OrdenResponse;
import com.api.mercadeando.dto.OrdenesResponse;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.service.OrdenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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

    /**
     * Encuentra una orden especificada y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     * @param ordenId Id de una Orden registrada
     * @return ResponseEntity<OrdenResponse> cuando la orden es encontrada y sus datos correspondientes
     */
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


    /**
     * Encuentra todos las ordenes y responde en JSON si se cuenta con el permiso
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<ClientesResponse> Con los clientes en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrdenesResponse> getClientes(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ){
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(ordenService.getOrdenes(offset,limit));
    }

    @PreAuthorize("hasAuthority('ADD_ORDEN')")
    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody @Valid OrdenRequest request){
        try{
            ordenService.createOrden(request);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
