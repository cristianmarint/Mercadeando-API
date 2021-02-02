package com.api.mercadeando.domain.service;


import com.api.mercadeando.domain.dto.RegisterRequest;
import com.api.mercadeando.infrastructure.persistence.entity.User;
import com.api.mercadeando.infrastructure.persistence.jpa.UserJPARepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author cristianmarint
 * @Date 2021-02-01 8:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserJPARepository userJPARepository;

    private final int id=4;
    private final String username = "testuserservice";
    private final String passwordPlain = "123456789";
    private final String passwordEncode = "$2a$10$iralKLjgFstqxQ6J2yIdV.QM3zwATgGZx0l7QAvka52MfhZGbO0bG"; // 123456789
    private final String email = "testuserservice@email.com";
    private final Timestamp createdAt = Timestamp.from(Instant.now());
    private final boolean activo = true;

    @Test
    public void signUp_ifAllDataIsValid() {
        RegisterRequest userSignup = new RegisterRequest().builder()
                .username(username)
                .email(email)
                .password(passwordPlain)
                .build();

        authService.signup(userSignup);
        User saved = userJPARepository.findByUsernameIgnoreCase(userSignup.getUsername());

        assertNotNull(saved);
        assertEquals(userSignup.getEmail(),saved.getEmail());
        assertEquals(userSignup.getUsername(),saved.getUsername());
    }
}
