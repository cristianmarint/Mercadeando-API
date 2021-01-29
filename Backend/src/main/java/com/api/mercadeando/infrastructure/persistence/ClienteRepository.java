package com.api.mercadeando.infrastructure.persistence;

import com.api.mercadeando.domain.data.ClienteData;
import com.api.mercadeando.domain.dto.ClienteRequest;
import com.api.mercadeando.domain.dto.ClienteResponse;
import com.api.mercadeando.domain.dto.ClientesResponse;
import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.entity.Cliente;
import com.api.mercadeando.infrastructure.persistence.entity.Orden;
import com.api.mercadeando.infrastructure.persistence.jpa.ClienteJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.OrdenJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.ClienteMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_ORDENES_V1;

/**
 * @author cristianmarint
 * @Date 2021-01-21 2:23
 */
@Repository
@AllArgsConstructor
@Slf4j
public class ClienteRepository implements ClienteData {
    @Autowired
    private final ClienteJPARepository clienteJPARepository;
    @Autowired
    private final OrdenJPARepository ordenJPARepository;
    @Autowired
    private final ClienteMapper clienteMapper;

    @Override
    public ClientesResponse readClientes(int offset, int limit) {
        List<Cliente> clientes = clienteJPARepository.getClientes(offset, limit);
        return clienteMapper.mapClientesToClienteResponse(clientes, offset, limit);
    }

    @Override
    public ClienteResponse readCliente(Long clienteId) throws ResourceNotFoundException, BadRequestException {
        if (clienteId == null) throw new BadRequestException("ClienteId no puede ser Null");
        Cliente cliente = clienteJPARepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException(clienteId, "Cliente"));

        List<Link> ordenesLinks = new ArrayList<>();
        for (Orden orden : ordenJPARepository.getClienteOrdenes(clienteId)) {
            ordenesLinks.add(new Link("orden", String.format(URL_ORDENES_V1 + "/", orden.getId())));
        }
        return clienteMapper.mapClienteToClienteResponse(cliente, ordenesLinks);
    }

    @Override
    public ClienteResponse addCliente(ClienteRequest request) throws BadRequestException {
        Cliente cliente = clienteJPARepository.save(clienteMapper.mapClienteRequestToCliente(request, null));
        return clienteMapper.mapClienteToClienteResponse(cliente, null);
    }

    @Override
    public void editCliente(Long clienteId, ClienteRequest request) throws ResourceNotFoundException, BadRequestException {
        if (clienteId == null) throw new BadRequestException("ClienteId no puede ser Null");
        Optional<Cliente> actual = clienteJPARepository.findById(clienteId);
        if (actual.isPresent()) {
            request.setId(clienteId);
            clienteJPARepository.save(clienteMapper.mapClienteRequestToCliente(request, actual.get()));
        } else {
            throw new ResourceNotFoundException(clienteId, "Cliente");
        }
    }

    @Override
    public void deactivateCliente(Long clienteId, boolean estado) throws BadRequestException, ResourceNotFoundException {
        if (clienteId == null) throw new BadRequestException("ClienteId no puede ser Null");
        if (clienteJPARepository.findById(clienteId).isPresent()) {
            clienteJPARepository.updateClienteEstado(clienteId, estado);
        } else {
            throw new ResourceNotFoundException(clienteId, "Cliente");
        }
    }
}
