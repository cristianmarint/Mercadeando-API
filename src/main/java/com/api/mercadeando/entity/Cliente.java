package com.api.mercadeando.entidades;

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
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Builder.Default
    private Boolean activo=true;

    @NotBlank(message = "El nombre del cliente ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido del cliente ser no puede estar Blank o Null")
    @Column(nullable = false)
    private String apellido;

    @Column(unique = true)
    private String cedula;

    private String direccion;

    private String ciudad;

    @Enumerated(EnumType.STRING)
    private ColombiaDepartamentos departamento;

//    @OneToMany
//    private Set<Orden> ordenes = new HashSet<>();
}
