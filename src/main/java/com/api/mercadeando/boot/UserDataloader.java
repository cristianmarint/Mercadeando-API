package com.api.mercadeando.boot;

import com.api.mercadeando.entidades.User;
import com.api.mercadeando.repositorio.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@Slf4j
public class UserDataloader implements CommandLineRunner {
    private final UserRepository userRepository;

    public UserDataloader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        log.info("---------- 1 - CARGANDO USERS ----------");
        userRepository.deleteAllInBatch();
        User user1 = new User()
                .builder()
                .email("cristianmarint@mail.com")
                .username("cristianmarint")
                .password("$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG")// 123456789
                .activo(true)
                .build();
        userRepository.save(user1);
    }
}
