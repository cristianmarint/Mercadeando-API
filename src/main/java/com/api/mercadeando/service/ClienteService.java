package com.api.mercadeando.service;

import com.api.mercadeando.entity.Cliente;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {

    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<Cliente> getClientes(int offset, int limit) throws MercadeandoException {
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        Pageable pageable = PageRequest.of(offset,limit);
//        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return (List<Cliente>) clienteRepository.findAll(pageable);
    }
}

