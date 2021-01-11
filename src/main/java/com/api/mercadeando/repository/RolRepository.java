package com.api.mercadeando.repository;

import com.api.mercadeando.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author cristianmarint
 * @Date 2021-01-11 10:42
 */
@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {
    Rol findByName(String name);
}
