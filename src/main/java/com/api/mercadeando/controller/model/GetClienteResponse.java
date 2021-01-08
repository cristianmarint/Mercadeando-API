package com.api.mercadeando.controller.model;

import com.api.mercadeando.entity.Cliente;
import com.api.mercadeando.entity.ColombiaDepartamentos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

/**
 *  API response para cuando se solicita un solo Cliente.
 */
@JsonPropertyOrder({
        "id",
        "activo",
        "nombres",
        "apellidos",
        "cedula",
        "direccion",
        "ciudad",
        "departamento"
})
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetClienteResponse {
    private Long id;
    private Boolean activo=true;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String ciudad;
    private ColombiaDepartamentos departamento;

    /**
     * Transforma el Cliente Entity a ClienteResponse para ser retornado por la API al frontend
     * @param cliente
     * @return
     */
    public static GetClienteResponse from(final Cliente cliente){
        GetClienteResponse response = new GetClienteResponse().builder()
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
}
