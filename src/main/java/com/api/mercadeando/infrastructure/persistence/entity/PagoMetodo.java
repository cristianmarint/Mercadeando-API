package com.api.mercadeando.infrastructure.persistence.entity;

public enum PagoMetodo {
    /**
     * Estado de la orden Orden.setEstado = PAGADO
     */
    EFECTIVO,
    /**
     * Estado de la orden Orden.setEstado = PENDIENTE
     */
    CHECK,
    /**
     * Estado de la orden Orden.setEstado = PENDIENTE
     */
    TARJETA_CREDITO,
    /**
     * Estado de la orden Orden.setEstado = PAGADO
     */
    TARJETA_DEBITO
}
