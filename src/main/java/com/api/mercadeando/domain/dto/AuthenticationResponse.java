package com.api.mercadeando.domain.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

/**
 * Respuesta de API para autentificación
 */
@JsonPropertyOrder({
        "username",
        "authenticationToken",
        "refreshToken",
        "expiresAt",
        "roles",
        "permisos"
})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String username;
    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;

    private Set<String> roles;
    private Set<String> permisos;
}
