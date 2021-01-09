package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenRepository extends JpaRepository<Orden,Long> {
//    List<Orden> findOrdenByUserId();
}
