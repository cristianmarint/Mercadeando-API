package com.api.mercadeando.mapper;

import com.api.mercadeando.domain.dto.ClienteRequest;
import com.api.mercadeando.domain.dto.ClienteResponse;
import com.api.mercadeando.domain.dto.ClientesResponse;
import com.api.mercadeando.domain.dto.Link;
import com.api.mercadeando.infrastructure.persistence.entity.Cliente;
import com.api.mercadeando.domain.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_CLIENTES_V1;

/**
 * Permite mapear datos internos de un Cliente
 * para mostrarlos de manera segura por medio de la API
 */
@Component
@AllArgsConstructor
@Slf4j
public class ClienteMapper {

    /**
     * Transforma el Cliente Entity a ClienteResponse para ser retornado por la API en formato JSON
     * @param cliente entidad con datos de un cliente
     * @param ordenesLinks Links a ordenes
     * @return ClienteResponse con detalles de cliente y links a ordenes
     */
    public ClienteResponse mapClienteToClienteResponse(Cliente cliente,List<Link> ordenesLinks){
        ClienteResponse response = new ClienteResponse();
        if (cliente.getId()!=null) response.setId(cliente.getId());
        if (cliente.getActivo()!=null) response.setActivo(cliente.getActivo());
        if (cliente.getNombres()!=null) response.setNombres(cliente.getNombres());
        if (cliente.getApellidos()!=null) response.setApellidos(cliente.getApellidos());
        if (cliente.getCedula()!=null) response.setCedula(cliente.getCedula().replace(".",""));
        if (cliente.getDireccion()!=null) response.setDireccion(cliente.getDireccion());
        if (cliente.getCiudad()!=null) response.setCiudad(cliente.getCiudad());
        if (cliente.getDepartamento()!=null) response.setDepartamento(cliente.getDepartamento());
        if (ordenesLinks!=null) response.setOrdenes(ordenesLinks);
        response.setSelf(new Link("self",String.format(URL_CLIENTES_V1+"/%s",cliente.getId())));
        return response;
    }

    /**
     * Transforma una lista de Cliente Entities a ClientesResponse para ser retornado por la API al frontend
     * @param clientes Lista de clientes con sus datos
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return ClientesResponse con clientes y sus datos correspondientes
     */
    public ClientesResponse mapClientesToClienteResponse(List<Cliente> clientes, int offset, int limit){
        ClientesResponse response = new ClientesResponse();
        if (clientes!=null & !clientes.isEmpty()){
            response.setCount(clientes.size());
            clientes.forEach(cliente -> response.getClientes().add(
                    this.mapClienteToClienteResponse(
                            cliente,
                            Collections.singletonList(new Link("orden",URL_CLIENTES_V1+"/"+cliente.getId()+"/ordenes"))
                    )));
            if (offset>0){
                response.getLinks().add(
                        new Link("prev",
                                String.format("/api/v1/clientes?offset=%s&limit=%s", Math.max(offset - limit, 0), limit)));
            }
            response.getLinks().add(
                    new Link("next",
                            String.format("/api/v1/clientes?offset=%s&limit=%s", offset + limit, limit)));
        }
        return response;
    }

    /**
     * Mapea un ClienteRequest a Cliente, si cliente es Null, creará una nueva instancia
     * @param request ClienteRequest
     * @param cliente Cliente entity
     * @return Cliente datos de cliente mapeado
     */
    public Cliente mapClienteRequestToCliente(ClienteRequest request, Cliente cliente) throws BadRequestException {
        if (request==null) throw new BadRequestException("ClienteRequest cannot be Null");
        if (cliente==null) cliente = new Cliente();

        if (request.getId()!=null) cliente.setId(request.getId());
        if (request.getActivo()!=null) cliente.setActivo(request.getActivo());
        if (request.getNombres()!=null) cliente.setNombres(request.getNombres());
        if (request.getApellidos()!=null) cliente.setApellidos(request.getApellidos());
        if (request.getCedula()!=null) cliente.setCedula(request.getCedula().replace(".",""));
        if (request.getDireccion()!=null) cliente.setDireccion(request.getDireccion());
        if (request.getCiudad()!=null) cliente.setCiudad(request.getCiudad());
        if (request.getDepartamento()!=null) cliente.setDepartamento(request.getDepartamento());

        return cliente;
    }
}
