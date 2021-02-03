package com.api.mercadeando.testdata;

import com.api.mercadeando.domain.dto.LoginRequest;
import com.api.mercadeando.domain.dto.SignupRequest;
import lombok.Getter;

/**
 * @author cristianmarint
 * @Date 2021-02-03 11:16
 */
@Getter
public class AuthTestData {
    private static final String USERNAME = "testuser";
    private static final String EMAIL = "testuser@email.com";
    private static final String PASSWORDPLAIN = "123456789";
    private static final String USERNAME_ADMIN = "admin";

    public SignupRequest generarUsuarioSignup() {
        return new SignupRequest().builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(PASSWORDPLAIN)
                .build();
    }

    public LoginRequest generarUsuarioLogin() {
        return new LoginRequest().builder()
                .username(USERNAME)
                .password(PASSWORDPLAIN)
                .build();
    }

    public LoginRequest usarUsuarioAdminLogin() {
        return new LoginRequest().builder()
                .username(USERNAME_ADMIN)
                .password(PASSWORDPLAIN)
                .build();
    }
}
