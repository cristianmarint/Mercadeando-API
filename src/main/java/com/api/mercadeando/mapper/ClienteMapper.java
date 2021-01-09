package com.api.mercadeando.mapper;

import com.api.mercadeando.dto.ClienteResponse;
import com.api.mercadeando.dto.ClientesResponse;
import com.api.mercadeando.dto.Link;
import com.api.mercadeando.entity.Cliente;
import org.springframework.lang.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_ORDENES_V1;
import static com.api.mercadeando.controller.Mappings.URL_CLIENTES_V1;

/**
 * Permite mapear datos internos para mostrarlos de manera segura
 * por medio de la API
 */
//TODO: USAR MAPSTRUCT
public class ClienteMapper {

    /**
     * Transforma el Cliente Entity a ClienteResponse para ser retornado por la API al frontend
     * @param cliente entidad con datos de un cliente
     * @param ordenesLinks Links a ordenes
     * @return
     */
    public ClienteResponse mapClienteToClienteResponse(Cliente cliente, @Nullable Map<String, Link> ordenesLinks){
        ClienteResponse response = new ClienteResponse().builder()
                .id(cliente.getId())
                .activo(cliente.getActivo())
                .nombres(cliente.getNombres())
                .apellidos(cliente.getApellidos())
                .cedula(cliente.getCedula())
                .direccion(cliente.getDireccion())
                .ciudad(cliente.getCiudad())
                .departamento(cliente.getDepartamento())
                .ordenes(ordenesLinks)
                .build();
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
        if (clientes!=null || !clientes.isEmpty()){
            response.setCount(clientes.size());

            clientes.forEach(cliente -> response.getClientes().add(
                    this.mapClienteToClienteResponse(
                            cliente,
                            Collections.singletonMap("_link",new Link("ordenes",URL_CLIENTES_V1+"/"+cliente.getId()+"/ordenes"))

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
}
