package com.api.mercadeando.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    @Nullable
    @Builder.Default
    private Boolean activo=true;

    @NotBlank(message = "El nombre del pruducto ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String nombre;

    @Lob
    private String descripcion;

    /**
     * En gramos
     */
    private Double peso;

    /**
     * Unidades
     */
    @Builder.Default
    private Integer unidades=0;

    /**
     * En Peso Colombiano CO
     */
    @Column(name = "precio", nullable = false)
    @DecimalMin(value = "0.00", message = "El precio debe de ser un valor mayor a cero (0)")
    @Builder.Default
    private BigDecimal precio= BigDecimal.valueOf(0);

    @OneToMany(cascade= CascadeType.ALL, orphanRemoval=true)
    @JoinTable(name = "producto_foto")
    private Set<FileStorage> fotos;

    @Builder.Default
    private ProductoEstado estado=ProductoEstado.DISPONIBLE;

    @Builder.Default
    private Instant createdAt= Instant.now();
}