package com.api.mercadeando.controller;

import com.api.mercadeando.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clientes")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getClientes(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ){
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(clienteService.getClientes(offset,limit));
    }

}
