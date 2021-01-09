package com.api.mercadeando.controller;

import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;

@RestController
@RequestMapping(URL_CLIENTES_V1)
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    /**
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
//
//    @GetMapping(value = "/{clienteId}",produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<ClienteResponse> getCliente(@PathVariable(value = "clienteId") String clienteId){
//
//    }

}
