package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden,Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM orden WHERE cliente_id=:clienteId ORDER BY created_at DESC"
    )
    List<Orden> getClienteOrdenes(@Param(value = "clienteId") Long clienteId);
}
