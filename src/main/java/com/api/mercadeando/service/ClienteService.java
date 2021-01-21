package com.api.mercadeando.service;

import com.api.mercadeando.dto.ClienteRequest;
import com.api.mercadeando.dto.ClienteResponse;
import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.dto.Link;
import com.api.mercadeando.entity.Cliente;
import com.api.mercadeando.entity.Orden;
import com.api.mercadeando.exception.BadRequestException;
import com.api.mercadeando.exception.MercadeandoException;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.mapper.ClienteMapper;
import com.api.mercadeando.repository.ClienteRepository;
import com.api.mercadeando.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;

/**
 * Brinda acceso a modificación y creación de
 * recursos
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private OrdenRepository ordenRepository;
    @Autowired
    private ClienteMapper clienteMapper;

    /**
     * Encuentra todos los clientes y responde en JSON si se cuenta con el permiso
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ClientesResponse Con los clientes en formato JSON
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    @Transactional(readOnly = true)
    public ClientesResponse readClientes(int offset, int limit){
        if (offset<0) throw new MercadeandoException("Offset must be greater than zero 0");
        if (limit<0) throw new MercadeandoException("Limit must be greater than zero 0");
        if (limit>100) throw new MercadeandoException("Offset must be less than one hundred 100");
        List<Cliente>clientes=clienteRepository.getClientes(offset, limit);
        return clienteMapper.mapClientesToClienteResponse(clientes,offset,limit);
    }

    /**
     * Encuentra un cliente especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @return ClienteResponse con datos correspondientes
     * @throws ResourceNotFoundException cuando el cliente no es encontrado
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    public ClienteResponse readCliente(Long clienteId) throws ResourceNotFoundException {
        if (clienteId==null) throw new MercadeandoException("ClienteId cannot be Null");

        Cliente cliente = clienteRepository.findById(clienteId).orElseThrow(()->new ResourceNotFoundException(clienteId,"Cliente"));

        List<Link> ordenesLinks=new ArrayList<>();
        for (Orden orden : ordenRepository.getClienteOrdenes(clienteId)) {
            ordenesLinks.add(new Link("orden", String.format(URL_CLIENTES_V1 + "/" + clienteId + "/ordenes/%s", orden.getId())));
        };
        return clienteMapper.mapClienteToClienteResponse(cliente,ordenesLinks);
    }

    /**
     * Permite crear un cliente especificando sus datos si se cuenta con el permiso
     * @param clienteRequest Datos necesarios para crear cliente
     * @throws BadRequestException cuando faltan datos necesario
     */
    @PreAuthorize("hasAuthority('ADD_CLIENTE')")
    public void addCliente(@Valid ClienteRequest clienteRequest) throws BadRequestException {
        validateCliente(clienteRequest);
        Cliente cliente = clienteRepository.save(clienteMapper.mapClienteRequestToCliente(clienteRequest,null));
    }

    /**
     * Actualiza los datos de un cliente registrado si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @param request ClienteRequest con los datos nuevos
     * @throws ResourceNotFoundException cuando el recuerso no existe
     * @throws BadRequestException cuando existen valores incorrectos.
     */
    @PreAuthorize("hasAuthority('EDIT_CLIENTE')")
    public void editCliente(Long clienteId, ClienteRequest request) throws ResourceNotFoundException, BadRequestException {
        validateCliente(request);
        if (clienteId == null) throw new BadRequestException("ClienteId cannot be Null");
        Optional<Cliente> actual = clienteRepository.findById(clienteId);
        if (actual.isPresent()){
            request.setId(clienteId);
            clienteRepository.save(clienteMapper.mapClienteRequestToCliente(request,actual.get()));
        }else {
            throw new ResourceNotFoundException(clienteId,"Cliente");
        }
    }


    /**
     * Actualiza el estado de un cliente registrado si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @param estado boolean que permite realizar softdelete
     * @throws BadRequestException cuando cliente ClienteId
     * @throws ResourceNotFoundException cuando el Cliente no esta registrado
     */
    @PreAuthorize("hasAuthority('DELETE_CLIENTE')")
    public void softDeleteCliente(Long clienteId, boolean estado) throws BadRequestException, ResourceNotFoundException {
        if(clienteId==null) throw new BadRequestException("ClienteId cannot be Null");
        if (clienteRepository.findById(clienteId).isPresent()) {
            clienteRepository.updateClienteEstado(clienteId,estado);
        }else {
            throw new ResourceNotFoundException(clienteId,"Cliente");
        }
    }

    /**
     * Permite validar los campos con caracteristica Notnull
     * @param clienteRequest entidad a verificar
     */
    private void validateCliente(ClienteRequest clienteRequest) throws BadRequestException {
        if (clienteRequest.getNombres()==null) throw new BadRequestException("El nombre del cliente no puede ser Null");
        if (clienteRequest.getApellidos()==null) throw new BadRequestException("El apellido del cliente no puede ser Null");
    }

}

