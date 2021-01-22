package com.api.mercadeando.infrastructure.persistence.entity;

import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
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

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(targetEntity = Categoria.class)
    @JoinTable(
            name = "producto_categoria",
            joinColumns = @JoinColumn(name = "producto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new HashSet<>();
    public void agregarCategoria(Categoria categoria) {
        if (categorias == null){
            categorias = new HashSet<>(Collections.singleton(categoria));
        }else {
            categorias.add(categoria);
        }
    }

    public void eliminarCategoria(Categoria categoria) {
        if(categorias!=null) categorias.remove(categoria);
    }

    @Builder.Default
    private Instant createdAt= Instant.now();
}
