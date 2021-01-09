package com.api.mercadeando.service;

import com.api.mercadeando.dto.ClienteResponse;
import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.dto.Link;
import com.api.mercadeando.entity.Cliente;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.ClienteMapper;
import com.api.mercadeando.repository.ClienteRepository;
import com.api.mercadeando.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import static com.api.mercadeando.controller.Mappings.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OrdenRepository ordenRepository;

    /**
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ResposeEntity<ClientesResponse> Con los clientes en formato JSON
     */
    @Transactional(readOnly = true)
    public ClientesResponse getClientes(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Cliente>clientes=clienteRepository.getClientes(offset, limit);
        ClienteMapper clienteMapper = new ClienteMapper();
        return clienteMapper.mapClientesToClienteResponse(clientes,offset,limit);
    }

    /**
     * Encuentra un cliente especificado y retorna sus datos y ordenes asociadas
     * @param clienteId
     * @return ClienteResponse con datos correspondientes
     * @throws ResourceNotFoundException cuando el cliente no es encontrado
     */
    public ClienteResponse getCliente(Long clienteId) throws ResourceNotFoundException {
        if (clienteId==null) throw new MercadeandoException("ClienteId cannot be Null");

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(()->new ResourceNotFoundException(clienteId,"Cliente"));

        Map<String,Link> ordenesLinks=new HashMap<>();
        ListIterator<Orden> iterator = ordenRepository.getClienteOrdenes(clienteId).listIterator();
        while (iterator.hasNext()){
            Orden orden = iterator.next();
            ordenesLinks.put("orden-"+orden.getId(), new Link("orden",String.format(URL_CLIENTES_V1+"/"+clienteId+"/ordenes/%s",orden.getId())));
        }

        ClienteMapper clienteMapper = new ClienteMapper();
        return clienteMapper.mapClienteToClienteResponse(cliente,ordenesLinks);
    }
}

