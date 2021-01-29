package com.api.mercadeando.infrastructure.persistence.entity;

public enum ProductoEstado {
    /**
     * Cuando existe más de 10 unidades
     */
    DISPONIBLE,
    /**
     * Cuando existe 10 unidades o menos
     */
    POCAS_UNIDADES,
    /**
     * Cuando existe 0 unidaes
     */
    AGOTADO
}
