package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT producto.* FROM orden_producto INNER JOIN producto ON orden_producto.producto_id=producto.id WHERE orden_producto.orden_id=:ordenId ORDER BY created_at DESC"
    )
    List<Producto> getOrdenProductos(Long ordenId);
}
