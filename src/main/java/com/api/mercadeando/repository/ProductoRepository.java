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

    /**
     * Permite buscar a todos los productos
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return List<Producto> Listado de productos existentes
     */
    @Query(
            nativeQuery = true,
            value = "SELECT producto.* FROM producto ORDER BY producto.created_at ASC LIMIT :limit OFFSET :offset"
    )
    List<Producto> getProductos(int offset, int limit);
}
