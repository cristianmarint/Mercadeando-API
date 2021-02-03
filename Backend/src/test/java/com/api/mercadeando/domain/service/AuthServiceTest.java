package com.api.mercadeando.domain.service;


import com.api.mercadeando.domain.dto.AuthenticationResponse;
import com.api.mercadeando.domain.dto.LoginRequest;
import com.api.mercadeando.domain.dto.SignupRequest;
import com.api.mercadeando.infrastructure.persistence.entity.User;
import com.api.mercadeando.infrastructure.persistence.jpa.UserJPARepository;
import com.api.mercadeando.testdata.AuthTestData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author cristianmarint
 * @Date 2021-02-01 8:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthServiceTest {
    private final SignupRequest usuarioSignup = new AuthTestData().generarUsuarioSignup();
    private final LoginRequest usuarioLogin = new AuthTestData().generarUsuarioLogin();
    @Autowired
    private AuthService authService;
    @Autowired
    private UserJPARepository userJPARepository;

    @Test
    public void signUp_Activate_ifAllDataIsValid() {
        if (userJPARepository.findByUsernameIgnoreCase(usuarioSignup.getUsername()) != null) return;

        authService.signup(usuarioSignup);

        User saved = userJPARepository.findByUsernameIgnoreCase(usuarioSignup.getUsername());
        saved.setActivo(true);
        userJPARepository.save(saved);

        assertNotNull(saved);
        assertEquals(usuarioSignup.getEmail(), saved.getEmail());
        assertEquals(usuarioSignup.getUsername(), saved.getUsername());
    }

    @Test
    public void login_ifAllDataIsValid() {
        if (userJPARepository.findByUsernameIgnoreCase(usuarioLogin.getUsername()) == null)
            authService.signup(usuarioSignup);

        User saved = userJPARepository.findByUsernameIgnoreCase(usuarioSignup.getUsername());
        AuthenticationResponse response = authService.login(usuarioLogin);

        assertNotNull(response.getExpiresAt());
        assertNotNull(response.getAuthenticationToken());
        assertNotNull(response.getRefreshToken());
        assertEquals(usuarioLogin.getUsername(), response.getUsername());
        assertEquals("[USUARIO]", response.getRoles().toString());
    }
}
