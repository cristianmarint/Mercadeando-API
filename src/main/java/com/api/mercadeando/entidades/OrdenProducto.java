package com.api.mercadeando.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdenProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id",nullable = false,updatable = false)
    private Orden ordenProducto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id",nullable = false)
    private Producto producto;

}
