package com.api.mercadeando.infrastructure.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

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

    @Column(nullable = false)
    private BigDecimal precio= BigDecimal.valueOf(0);

    @ManyToOne(optional = false)
    @JoinColumn(name = "orden_id",nullable = false,updatable = false)
    private Orden ordenx;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id",nullable = false)
    private Producto producto;

}
