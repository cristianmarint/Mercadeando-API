package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJPARepository extends JpaRepository<User, Long> {
    /**
     * Permite buscar un usuario por username
     * @param username nombre de usuario unico
     * @return Optional<User> usuario buscado
     */
    Optional<User> findByUsername(String username);

    /**
     * Permite buscar usuario por email ignorado mayusculas
     * @param email email de un usuario unico
     * @return User usuario encontrado
     */
    User findByEmailIgnoreCase(String email);

    /**
     * Permite buscar un usuario por username ignorado mayusculas
     * @param username nombre de usuario unico
     * @return User usuario buscado
     */
    User findByUsernameIgnoreCase(String username);
}
