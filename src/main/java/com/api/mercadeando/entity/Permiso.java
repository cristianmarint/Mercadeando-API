package com.api.mercadeando.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-11 9:55
 */
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * BROWSE:
     * READ: LEEAR UN REGISTRO
     * EDIT: MODIFICAR UN REGISTRO
     * ADD: CREAR UN REGISTRO
     * DELETE: BORRAR/SOFT UN REGISTRO
     */
    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "permisos", cascade = CascadeType.MERGE)
    private Set<Rol> roles;
}
