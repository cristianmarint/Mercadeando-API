package com.api.mercadeando.entidades;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Builder
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

    private String cedula;

    private String direccion;

//    @OneToMany
//    private Set<Orden> ordenes = new HashSet<>();
}
