package com.api.mercadeando.dto;

import com.api.mercadeando.entity.ColombiaDepartamentos;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 *  API request para cuando se crea un solo Cliente.
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
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequest {
    private Long id;
    private Boolean activo;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String direccion;
    private String ciudad;
    private ColombiaDepartamentos departamento;
    private static Instant updatedAt = Instant.now();
}
