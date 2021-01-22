package com.api.mercadeando.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Builder.Default
    private Boolean activo=true;

    @NotBlank(message = "El nombre del cliente ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "El apellido del cliente ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String apellidos;

    @Nullable
    @Column(unique = true)
    private String cedula;

    @Nullable
    private String direccion;

    @Nullable
    private String ciudad;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ColombiaDepartamentos departamento;

    @Builder.Default
    private Instant createdAt= Instant.now();

    @Builder.Default
    private Instant updatedAt= Instant.now();
}
