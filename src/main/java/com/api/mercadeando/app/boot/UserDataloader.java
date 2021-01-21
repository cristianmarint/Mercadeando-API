package com.api.mercadeando.app.boot;

import com.api.mercadeando.infrastructure.persistence.entity.Rol;
import com.api.mercadeando.infrastructure.persistence.entity.User;
import com.api.mercadeando.domain.exception.ResourceNotFoundException;
import com.api.mercadeando.infrastructure.persistence.jpa.RolJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.UserJPARepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Crea valores por defecto para usuarios en la BD
 */
@Component
@Order(1)
@AllArgsConstructor
@Slf4j
public class UserDataloader implements CommandLineRunner {
    @Autowired
    private final UserJPARepository userJPARepository;
    @Autowired
    private final RolJPARepository rolJPARepository;

    @Override
    public void run(String... args) throws ResourceNotFoundException {
        log.info("---------> 1 - CARGANDO USERS");
        userJPARepository.deleteAllInBatch();

        Rol rolAdmin = rolJPARepository.findByName("ADMIN");
        Rol rolCajero = rolJPARepository.findByName("CAJERO");
        Rol rolUsuario = rolJPARepository.findByName("USUARIO");

        User user1 = new User()
                .builder()
                .email("admin@mercadeando.com")
                .username("admin")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        rolAdmin.addUser(user1);
        rolCajero.addUser(user1);
        user1.addRol(rolAdmin);
        user1.addRol(rolCajero);
        userJPARepository.save(user1);

        User user2 = new User()
                .builder()
                .email("cajero@mercadeando.com")
                .username("cajero")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        rolCajero.addUser(user2);
        user2.addRol(rolCajero);
        userJPARepository.save(user2);

        User user3 = new User()
                .builder()
                .email("usuario@mercadeando.com")
                .username("usuario")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        rolUsuario.addUser(user3);
        user3.addRol(rolUsuario);
        userJPARepository.save(user3);
    }
}
