package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author cristianmarint
 * @Date 2021-01-11 10:42
 */
@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {
    Rol findByName(String name);

    @Query(
            nativeQuery = true,
            value = "SELECT rol.* FROM user INNER JOIN user_rol ON user_rol.user_id=user.id INNER JOIN rol ON rol.id=user_rol.rol_id WHERE user.username=:username"
    )
    Set<Rol> findByUsername(String username);
}
