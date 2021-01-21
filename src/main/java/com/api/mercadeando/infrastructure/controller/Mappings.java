package com.api.mercadeando.infrastructure.controller;

import lombok.NoArgsConstructor;

/**
 * Mapeado para URL de la API
 */
@NoArgsConstructor
public final class Mappings {
    public static final String URL_BASE_V1 = "/api/v1";
    public static final String URL_AUTH_V1 = "/api/v1/auth/";
    public static final String URL_CLIENTES_V1 = "/api/v1/clientes";
    public static final String URL_CLIENTES_ORDENES_V1 = "/api/v1/clientes/{clienteId}/ordenes";
    public static final String URL_PRODUCTOS_V1 = "/api/v1/productos";
    public static final String URL_ORDENES_V1 = "/api/v1/ordenes";
    public static final String URL_STATIC_FILE_V1 = "/api/v1/static";
}
