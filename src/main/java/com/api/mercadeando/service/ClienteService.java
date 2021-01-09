package com.api.mercadeando.service;

import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.entity.Cliente;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.mapper.ClienteMapper;
import com.api.mercadeando.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public ClientesResponse getClientes(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Cliente>clientes=clienteRepository.getClientes(offset, limit);
        ClienteMapper clienteMapper = new ClienteMapper();
        return clienteMapper.mapClientesToClienteResponse(clientes,offset,limit);
    }
}

