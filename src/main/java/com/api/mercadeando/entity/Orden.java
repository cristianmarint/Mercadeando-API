package com.api.mercadeando.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * False = borrado con softdelete
     */
    @Builder.Default
    private Boolean activo = true;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrdenEstado estado=OrdenEstado.PENDIENTE;

    @Builder.Default
    private Instant fecha=null;

    @Builder.Default
    private BigDecimal total = null;

    @Builder.Default
    private Instant createdAt= Instant.now();

    @OneToMany(mappedBy = "ordenx",cascade = CascadeType.ALL)
    private Set<OrdenProducto> productos = new HashSet<>();

    @OneToOne()
    private Pago pago;

    @Nullable
    @ManyToOne(optional = true)
    private Cliente cliente;

    @ManyToOne(optional = false)
    private User user;
}
