package com.api.mercadeando.dto;

import com.api.mercadeando.entity.ColombiaDepartamentos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 *  API response para cuando se solicita un solo ICliente.
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
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {
    private Long id;
    private Boolean activo=true;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String ciudad;
    private ColombiaDepartamentos departamento;

    private Map<String,Link> ordenes;
}
