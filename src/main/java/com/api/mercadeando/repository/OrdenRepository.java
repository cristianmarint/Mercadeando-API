package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden,Long> {
    /**
     * @param clienteId Id de un cliente registrado y con almenos una orden
     * @return List<Orden> Listado de ordenes
     */
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orden WHERE cliente_id=:clienteId ORDER BY created_at DESC"
    )
    List<Orden> getClienteOrdenes(@Param(value = "clienteId") Long clienteId);

    /**
     * Permite buscar a todas las ordenes
     * @param offset Punto de partida mayor a cero para buscar nuevos valores
     * @param limit Cantidad de valores a entontrar menor a cien
     * @return List<Orden> Listado de ordenes existentes
     */
    @Query(
            nativeQuery = true,
            value = "SELECT orden.* FROM orden ORDER BY orden.created_at ASC LIMIT :limit OFFSET :offset"
    )
    List<Orden> getOrdenes(int offset, int limit);

    /**
     * Cambia el estado de una orden
     * @param ordenId Id de una orden registrada
     * @param estado
     */
    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE orden SET activo =:estado WHERE id=:ordenId"
    )
    void updateOrdenEstado(Long ordenId, boolean estado);
}
