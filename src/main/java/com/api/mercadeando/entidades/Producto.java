package com.api.mercadeando.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Builder.Default
    private Boolean activo=true;

    @NotBlank(message = "El nombre del pruducto ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String nombre;

    @Lob
    private String descripcion;

    @Builder.Default
    private Integer cantidad=0;

    @NotBlank(message = "El precio del pruducto ser puede estar Blank")
    @NotNull(message = "El precio del pruducto ser puede estar Null")
    @Column(name = "precio", nullable = false)
    @DecimalMin(value = "0.00", message = "El precio debe de ser un valor mayor a cero (0)")
    @Builder.Default
    private BigDecimal precio= BigDecimal.valueOf(0);

    @Column(length = 2000)
    private String foto;

    @Builder.Default
    private Boolean estado=true;

    @Builder.Default
    private Instant createdAt= Instant.now();
}
