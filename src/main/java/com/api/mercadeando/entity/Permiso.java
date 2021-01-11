package com.api.mercadeando.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-11 9:55
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "permisos", cascade = CascadeType.MERGE)
    private List<Rol> roles;
}
