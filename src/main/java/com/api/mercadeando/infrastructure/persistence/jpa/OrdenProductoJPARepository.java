package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.OrdenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenProductoRepository extends JpaRepository<OrdenProducto, Long> {
    /**
     * Permite buscar los detalles asociados a un producto en una orden
     * @param ordenId Id de una orden existente
     * @return Listado de detalles para una orden
     */
    @Query(
            nativeQuery = true,
            value = "SELECT orden_producto.id, orden_producto.cantidad, orden_producto.precio,orden_producto.orden_id, orden_producto.producto_id " +
                    "FROM orden_producto " +
                    "WHERE orden_producto.orden_id=:ordenId ORDER BY orden_producto.producto_id DESC"
    )
    List<OrdenProducto> getOrdenProductoDetalles(Long ordenId);
}
