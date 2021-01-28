package com.api.mercadeando.domain.service;

import com.api.mercadeando.domain.dto.AuthenticationResponse;
import com.api.mercadeando.domain.dto.LoginRequest;
import com.api.mercadeando.domain.dto.RefreshTokenRequest;
import com.api.mercadeando.domain.dto.RegisterRequest;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.infrastructure.persistence.entity.*;
import com.api.mercadeando.infrastructure.persistence.jpa.PermisoJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.RolJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.UserJPARepository;
import com.api.mercadeando.infrastructure.persistence.jpa.VerificationTokenJPARepository;
import com.api.mercadeando.infrastructure.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static com.api.mercadeando.infrastructure.controller.Mappings.URL_AUTH_V1;

/**
 * Service de autentificación de usuarios
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserJPARepository userJPARepository;
    private final VerificationTokenJPARepository verificationTokenJPARepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final RolJPARepository rolJPARepository;
    private final PermisoJPARepository permisoJPARepository;

    /**
     * Permite registrar un usuario y envia correo para verificar cuenta
     *
     * @param registerRequest Contiene username, email, y password
     */
    public void signup(RegisterRequest registerRequest) {

        if (userJPARepository.findByEmailIgnoreCase(registerRequest.getEmail()) == null && userJPARepository.findByUsernameIgnoreCase(registerRequest.getUsername()) == null) {
            User user = User
                    .builder()
                    .username(registerRequest.getUsername())
                    .email(registerRequest.getEmail())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .build();
            user.addRol(rolJPARepository.findByName("USUARIO"));
            userJPARepository.save(user);
            String token = generateVerificationToken(user);
            mailService.sendMail(new NotificationEmail(
                    "Por favor active su cuenta", user.getEmail(),
                    "Gracias por registrarse en Mercadeando,  por favor acceda a el enlace para activar su cuenta: " +
                            "http://localhost:8080" + URL_AUTH_V1 + "account-verification/" + token));
        } else {
            throw new MercadeandoException("Correo en uso " + registerRequest.getEmail());
        }
    }

    /**
     * Permite conocer el usuario de en una petición
     *
     * @return User usuario de petición actual
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userJPARepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username " + principal.getUsername()));
    }

    /**
     * Permite activar la cuenta de un usuario
     *
     * @param verificationToken token de verificacion con datos de usuario
     */
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userJPARepository.findByUsername(username).orElseThrow(() -> new MercadeandoException("Usuario no encontrado con username: " + username));
        user.setActivo(true);
        userJPARepository.save(user);
    }

    /**
     * Permite generar token de verificación
     *
     * @param user usuario registrado
     * @return String token
     */
    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenJPARepository.save(verificationToken);
        return token;
    }

    /**
     * Permite activar una cuenta de usuario basado en el token
     *
     * @param token token de verificación
     */
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenJPARepository.findByToken(token);
        fetchUserAndEnable(verificationToken.orElseThrow(() -> new MercadeandoException("Invalid Token")));
    }

    /**
     * Permite autentificar un usuario activo via credenciales correctas
     *
     * @param loginRequest Contiene username y password
     * @return AuthenticationResponse con datos de acceso
     */
    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();

        Set<Rol> rolSet = rolJPARepository.findByUsername(loginRequest.getUsername());
        Set<String> permisos = new HashSet<>();
        Set<String> roles = new HashSet<>();

        for (Rol r : rolSet) {
            roles.add(r.getName());
            Set<Permiso> tempo = permisoJPARepository.findByRolName(r.getName());
            for (Permiso p : tempo) {
                permisos.add(p.getName());
            }
        }
        response.setRoles(roles);
        response.setPermisos(permisos);

        return response;
    }

    /**
     * Permite generar un nuevo Bearer token
     *
     * @param refreshTokenRequest Contiene username y refreshToken
     * @return AuthenticationResponse con datos de acceso
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUserName(refreshTokenRequest.getUsername());
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .username(refreshTokenRequest.getUsername())
                .build();
    }

    /**
     * Permite verificar si un usuario tiene un sesion activa
     *
     * @return boolean
     */
    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }
}
