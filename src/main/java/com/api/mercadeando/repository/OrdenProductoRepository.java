package com.api.mercadeando.repository;

import com.api.mercadeando.entity.OrdenProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenProductoRepository extends JpaRepository<OrdenProducto, Long> {
}
