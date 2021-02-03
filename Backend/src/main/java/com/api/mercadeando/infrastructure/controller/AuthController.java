package com.api.mercadeando.infrastructure.controller;

import com.api.mercadeando.domain.dto.AuthenticationResponse;
import com.api.mercadeando.domain.dto.LoginRequest;
import com.api.mercadeando.domain.dto.RefreshTokenRequest;
import com.api.mercadeando.domain.dto.SignupRequest;
import com.api.mercadeando.domain.service.AuthService;
import com.api.mercadeando.domain.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_AUTH_V1;
import static org.springframework.http.HttpStatus.OK;

/**
 * Controlador para rutas de autentificación
 */
@RestController
@RequestMapping(value = URL_AUTH_V1)
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Enruta una petición de registro de usuario al Service
     *
     * @param signupRequest En el cuerpo contiene username, email y password
     * @return ResponseEntity Contiene una respuesta exitosa
     */
    @PostMapping("signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return new ResponseEntity<>("User Registration Successful", OK);
    }

    /**
     * Enruta una petición de ingreso al Service
     *
     * @param loginRequest En el cuerpo contiene username y password
     * @return AuthenticationResponse Contiene datos de un ingreso exitoso
     */
    @PostMapping("login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * Enruta una petición de verificar cuenta via token al Service
     *
     * @param token String que activa la cuenta via Email
     * @return ResponseEntity<String> Contiene respuesta exitosa
     */
    @GetMapping("account-verification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", OK);
    }

    /**
     * Genera un nuevo token Bearer de autentificación
     *
     * @param refreshTokenRequest En el cuerpo contiene refreshToken y username
     * @return AuthenticationResponse
     */
    @PostMapping("refresh-token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    /**
     * Cierra la sesión del usuario
     *
     * @param refreshTokenRequest En el cuerpo contiene refreshToken y username
     * @return AuthenticationResponse<String> Mensaje de cierro exitoso
     */
    @PostMapping("logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }
}
