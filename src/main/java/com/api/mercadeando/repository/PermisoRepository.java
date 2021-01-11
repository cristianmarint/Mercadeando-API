package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cristianmarint
 * @Date 2021-01-11 11:03
 */
@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
}
