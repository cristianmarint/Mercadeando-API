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
    private Currency currency;

    private String total;

    private PagoMetodo metodo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PagoMetodo pagoMetodo;
}
