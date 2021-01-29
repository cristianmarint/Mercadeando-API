package com.api.mercadeando.domain.dto;

import com.api.mercadeando.infrastructure.persistence.entity.ColombiaDepartamentos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * API response para cuando se solicita un solo Cliente.
 */
@JsonPropertyOrder({
        "id",
        "self",
        "activo",
        "nombres",
        "apellidos",
        "cedula",
        "direccion",
        "ciudad",
        "departamento"
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponse {
    private Long id;
    private Link self;
    private Boolean activo;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String ciudad;
    private ColombiaDepartamentos departamento;

    private List<Link> ordenes;
}
