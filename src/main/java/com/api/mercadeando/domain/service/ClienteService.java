package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.data.ClienteData;
import com.api.mercadeando.domain.dto.ClienteRequest;
import com.api.mercadeando.domain.dto.ClienteResponse;
import com.api.mercadeando.domain.dto.ClientesResponse;
import com.api.mercadeando.domain.exception.BadRequestException;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.jpa.OrdenJPARepository;
import com.api.mercadeando.infrastructure.persistence.mapper.ClienteMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

/**
 * Brinda acceso a modificación y creación de
 * recursos
 */
@Service
@AllArgsConstructor
@NoArgsConstructor
public class ClienteService {

    @Autowired
    private ClienteData clienteData;
    @Autowired
    private OrdenJPARepository ordenJPARepository;
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
        return clienteData.readClientes(offset,limit);
    }

    /**
     * Encuentra un cliente especificado y retorna sus datos y ordenes asociadas si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @return ClienteResponse con datos correspondientes
     * @throws ResourceNotFoundException cuando el cliente no es encontrado
     */
    @PreAuthorize("hasAuthority('READ_CLIENTE')")
    public ClienteResponse readCliente(Long clienteId) throws ResourceNotFoundException, BadRequestException {
        if (clienteId==null) throw new MercadeandoException("ClienteId no puede ser Null");
        return clienteData.readCliente(clienteId);
    }

    /**
     * Permite crear un cliente especificando sus datos si se cuenta con el permiso
     * @param request Datos necesarios para crear cliente
     * @throws BadRequestException cuando faltan datos necesario
     * @return ClienteResponse con los datos en formato JSON
     */
    @PreAuthorize("hasAuthority('ADD_CLIENTE')")
    public ClienteResponse addCliente(@Valid ClienteRequest request) throws BadRequestException {
        validarCliente(request);
        return clienteData.addCliente(request);
    }

    /**
     * Permite actualizar los datos de un cliente registrado si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @param request ClienteRequest con los datos modificados
     * @throws ResourceNotFoundException cuando el recuerso no existe
     * @throws BadRequestException cuando existen valores incorrectos.
     */
    @PreAuthorize("hasAuthority('EDIT_CLIENTE')")
    public void editCliente(Long clienteId, ClienteRequest request) throws ResourceNotFoundException, BadRequestException {
        validarCliente(request);
        clienteData.editCliente(clienteId, request);
    }


    /**
     * Actualiza el estado de un cliente registrado si se cuenta con el permiso
     * @param clienteId Id de un cliente registrado
     * @param estado boolean que permite realizar softdelete
     * @throws BadRequestException cuando cliente ClienteId
     * @throws ResourceNotFoundException cuando el Cliente no esta registrado
     */
    @PreAuthorize("hasAuthority('DELETE_CLIENTE')")
    public void deactivateCliente(Long clienteId, boolean estado) throws BadRequestException, ResourceNotFoundException {
        clienteData.deactivateCliente(clienteId,estado);
    }

    /**
     * Permite validar campos necesarios
     * @param request entidad a verificar
     * @throws BadRequestException cuando existen valores en NUll
     */
    private void validarCliente(ClienteRequest request) throws BadRequestException {
        if (request.getNombres()==null) throw new BadRequestException("El nombre del cliente no puede ser Null");
        if (request.getApellidos()==null) throw new BadRequestException("El apellido del cliente no puede ser Null");
    }

}

