package com.api.mercadeando.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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

    @Column(unique = true)
    private String cedula;

    private String direccion;

    private String ciudad;

    @Enumerated(EnumType.STRING)
    private ColombiaDepartamentos departamento;

//    @OneToMany
//    private Set<Orden> ordenes = new HashSet<>();
}
