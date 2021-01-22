package com.api.mercadeando.domain.data;

import com.api.mercadeando.domain.dto.ClienteRequest;
import com.api.mercadeando.domain.dto.ClienteResponse;
import com.api.mercadeando.domain.dto.ClientesResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;

/**
 * @author cristianmarint
 * @Date 2021-01-21 2:19
 */
public interface ClienteData {

    ClientesResponse getClientes(int offset, int limit);

    ClienteResponse getCliente(Long clienteId) throws ResourceNotFoundException, BadRequestException;

    ClienteResponse addCliente(ClienteRequest request) throws BadRequestException;

    void editCliente(Long clienteId, ClienteRequest request) throws ResourceNotFoundException, BadRequestException;

    void deactivateCliente(Long clienteId, boolean estado) throws BadRequestException, ResourceNotFoundException;
}
