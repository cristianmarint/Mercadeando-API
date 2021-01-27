package com.api.mercadeando.infrastructure.persistence.entity;

/**
 * @author cristianmarint
 * @Date 2021-01-25 9:12
 */
public enum Moneda {

    EUR("Euro Member Countries"),
    USD("United States Dollar"),
    COP("Colombia Peso");

    private String descripcion;

    Moneda(String description) {
        this.descripcion = description;
    }

    public String getDescripcion() {
        return descripcion;
    }
}