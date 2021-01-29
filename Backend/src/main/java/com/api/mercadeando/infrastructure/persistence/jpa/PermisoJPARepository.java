package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-11 11:03
 */
@Repository
public interface PermisoJPARepository extends JpaRepository<Permiso, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT permiso.* FROM rol INNER JOIN rol_permiso ON rol_permiso.roles_id=rol.id INNER JOIN permiso ON permiso.id=rol_permiso.permisos_id WHERE rol.name=:rolName"
    )
    Set<Permiso> findByRolName(String rolName);
}
