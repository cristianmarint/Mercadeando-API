package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.ClienteRequest;
import com.api.mercadeando.domain.dto.ClientesResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.domain.service.ClienteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CLIENTES_V1;

@RestController
@RequestMapping(URL_CLIENTES_V1)
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Encuentra todos los clientes y responde en JSON si se cuenta con el permiso
     *
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit  Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<ClientesResponse> Con los clientes en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientesResponse> readClientes(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int limit
    ) {
        if (offset < 0) offset = 0;
        if (limit < 0) limit = 5;
        if (limit > 100) limit = 100;

        return ResponseEntity.ok().body(clienteService.readClientes(offset, limit));
    }

    /**
     * Encuentra un cliente especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     *
     * @param clienteId Id de un cliente registrado
     * @return ResponseEntity<ClienteResponse> cuando el cliente es encontrado y sus datos correspondientes
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    @GetMapping(value = "/{clienteId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity readCliente(@PathVariable(value = "clienteId") @Min(1) Long clienteId) {
        try {
            if (clienteId == null) throw new BadRequestException("ClienteId no puede ser null");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(clienteService.readCliente(clienteId));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Permite crear un cliente especificando sus datos si se cuenta con el permiso
     *
     * @param request ClienteRequest con datos necesarios para crear un cliente
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('ADD_CLIENTE')")
    @PostMapping
    public ResponseEntity addCliente(@RequestBody @Valid ClienteRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.addCliente(request));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Permite actualizar un cliente registrado si se cuenta con el permiso
     *
     * @param clienteId Id de un cliente registrado
     * @param request   ClienteRequest con los datos nuevos
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('EDIT_CLIENTE')")
    @PutMapping(value = "/{clienteId}")
    public ResponseEntity<String> editCliente(@PathVariable("clienteId") @Min(1) Long clienteId, @RequestBody @Valid ClienteRequest request) {
        try {
            clienteService.editCliente(clienteId, request);
            return ResponseEntity.ok().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    /**
     * Permite actializar el estado de un cliente (Softdelete) si se cuenta con el permiso
     *
     * @param clienteId Id de un cliente registrado
     * @param estado    Nuevo estado de un cliente
     * @return HttpStatus Estado Http según corresponda
     */
    @PreAuthorize("hasAuthority('DELETE_CLIENTE')")
    @DeleteMapping(value = "/{clienteId}")
    public ResponseEntity deactivateCliente(@PathVariable("clienteId") @Min(1) Long clienteId, @RequestParam(value = "estado", required = true, defaultValue = "0") boolean estado) {
        try {
            clienteService.deactivateCliente(clienteId, estado);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
