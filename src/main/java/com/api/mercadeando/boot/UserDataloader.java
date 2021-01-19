package com.api.mercadeando.boot;

import com.api.mercadeando.entity.Rol;
import com.api.mercadeando.entity.User;
import com.api.mercadeando.exception.ResourceNotFoundException;
import com.api.mercadeando.repository.RolRepository;
import com.api.mercadeando.repository.UserRepository;
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
    private final UserRepository userRepository;
    @Autowired
    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws ResourceNotFoundException {
        log.info("---------> 1 - CARGANDO USERS");
        userRepository.deleteAllInBatch();

        Rol rolAdmin = rolRepository.findByName("ADMIN");
        Rol rolCajero = rolRepository.findByName("CAJERO");
        Rol rolUsuario = rolRepository.findByName("USUARIO");

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
        userRepository.save(user1);

        User user2 = new User()
                .builder()
                .email("cajero@mercadeando.com")
                .username("cajero")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        rolCajero.addUser(user2);
        user2.addRol(rolCajero);
        userRepository.save(user2);

        User user3 = new User()
                .builder()
                .email("usuario@mercadeando.com")
                .username("usuario")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        rolUsuario.addUser(user3);
        user3.addRol(rolUsuario);
        userRepository.save(user3);
    }
}
