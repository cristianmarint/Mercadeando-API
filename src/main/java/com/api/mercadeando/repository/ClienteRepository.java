package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT cliente.* FROM cliente ORDER BY cliente.cedula ASC LIMIT :limit OFFSET :offset"
    )
    List<Cliente> getClientes(@Param("offset") int offset,@Param("limit") int limit);
}
