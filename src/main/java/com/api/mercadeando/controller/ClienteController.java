package com.api.mercadeando.controller;

import com.api.mercadeando.dto.ClienteResponse;
import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;

@RestController
@RequestMapping(URL_CLIENTES_V1)
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    /**
     * Encuentra todos los clientes y responde en JSON
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<ClientesResponse> Con los clientes en formato JSON
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientesResponse> getClientes(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ){
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(clienteService.getClientes(offset,limit));
    }

    /**
     * Encuentra un cliente especificado y retorna sus datos y ordenes asociadas
     * @param clienteId Id de un cliente registrado
     * @return ResponseEntity<ClienteResponse> cuando el cliente es encontrado y sus datos correspondientes
     */
    @GetMapping(value = "/{clienteId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteResponse> getCliente(@PathVariable(value = "clienteId") @Min(1) Long clienteId) {
        try{
            if(clienteId == null) throw new BadRequestException();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(clienteService.getCliente(clienteId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
