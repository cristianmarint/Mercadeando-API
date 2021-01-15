package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    /**
     * Permite buscar a los productos en una orden
     * @param ordenId Id de una orden existente
     * @return Listado de productos para una orden
     */
    @Query(
            nativeQuery = true,
            value = "SELECT  producto.id, producto.nombre " +
                    "FROM orden_producto INNER JOIN producto ON orden_producto.producto_id=producto.id " +
                    "WHERE orden_producto.orden_id=:ordenId ORDER BY producto.id DESC"
    )
    List<Producto> getOrdenProductos(Long ordenId);
}
