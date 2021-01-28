package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ClienteJPARepository extends JpaRepository<Cliente, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT cliente.* FROM cliente ORDER BY cliente.created_at ASC LIMIT :limit OFFSET :offset"
    )
    List<Cliente> getClientes(@Param("offset") int offset, @Param("limit") int limit);

    @Transactional
    @Modifying
    @Query(
            nativeQuery = true,
            value = "UPDATE cliente SET activo =:estado WHERE id=:clienteId"
    )
    void updateClienteEstado(Long clienteId, boolean estado);
}
