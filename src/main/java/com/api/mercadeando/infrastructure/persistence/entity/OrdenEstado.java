package com.api.mercadeando.infrastructure.persistence.model;

public enum OrdenEstado {
    /**
     * La orden fue procesada y pagada correctamente
     */
    PAGADO,
    /**
     * Existen procesos pendientes con la orden Ej: Pago con tarjeta de credito, Empacando orden.
     */
    PROCESANDO,
    /**
     * No se a iniciado el proceso
     */
    PENDIENTE,
    /**
     * La orden fue cancelada, softdelete
     */
    CANCELADA
}
