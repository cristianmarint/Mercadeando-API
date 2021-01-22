package com.api.mercadeando.infrastructure.persistence.entity;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-22 10:11
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoria no puede ser NUll")
    private String nombre;

    @Nullable
    private String descripcion;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @ManyToMany(mappedBy = "categorias",targetEntity = Producto.class, fetch=FetchType.EAGER,cascade = CascadeType.REMOVE)
    private Set<Producto> productos = new HashSet<>();

    public void agregarProducto(Producto producto){
        if (productos == null){
            productos = new HashSet<>(Collections.singleton(producto));
        }else {
            productos.add(producto);
        }
    }

    public void eliminarProducto(Producto producto) {
        if(productos!=null){
            productos.remove(producto);
        }
    }
}
