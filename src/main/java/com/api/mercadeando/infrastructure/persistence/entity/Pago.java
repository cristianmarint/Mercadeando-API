package com.api.mercadeando.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "moneda")
    private Moneda moneda;

    private String total;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PagoMetodo metodo;

    @OneToOne(mappedBy = "pagox", cascade = CascadeType.ALL,optional = false)
    private Orden orden;

    @ManyToOne(optional = false)
    private User user;

}
