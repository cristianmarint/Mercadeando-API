package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.ClienteResponse;
import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.dto.Link;
import com.api.mercadeando.entity.Cliente;

import java.util.List;

public class ClienteMapper {
    /**
     * Transforma el Cliente Entity a ClienteResponse para ser retornado por la API al frontend
     * @param cliente
     * @return
     */
    public ClienteResponse mapClienteToClienteResponse (final Cliente cliente){
        ClienteResponse response = new ClienteResponse().builder()
                .id(cliente.getId())
                .activo(cliente.getActivo())
                .nombres(cliente.getNombres())
                .apellidos(cliente.getApellidos())
                .cedula(cliente.getCedula())
                .direccion(cliente.getDireccion())
                .ciudad(cliente.getCiudad())
                .departamento(cliente.getDepartamento())
                .build();
        return response;
    }

    /**
     * Transforma una lista de Cliente Entities a ClientesResponse para ser retornado por la API al frontend
     * @param clientes
     * @param offset
     * @param limit
     * @return
     */
    public ClientesResponse mapClientesToClienteResponse(List<Cliente> clientes, int offset, int limit){
        ClientesResponse response = new ClientesResponse();
        if (clientes!=null || !clientes.isEmpty()){
            response.setCount(clientes.size());
            clientes.forEach(cliente -> response.getClientes().add(this.mapClienteToClienteResponse(cliente)));
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
}
