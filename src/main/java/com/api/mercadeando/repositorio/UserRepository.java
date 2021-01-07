package com.api.mercadeando.repositorio;

import com.api.mercadeando.entidades.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    User findByEmailIgnoreCase(String email);
    User findByUsernameIgnoreCase(String username);
}