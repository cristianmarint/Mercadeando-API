package com.api.mercadeando.app.security;

import com.api.mercadeando.domain.exception.MercadeandoException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.Date;
import java.time.Instant;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

/**
 * Proporciona funcionalidad para hacer uso del JWT
 * por medio de una almacenamiento seguro en un JKS
 */
@Service
public class JwtProvider {

    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    private static final String SECRECT = "123456789";

    /**
     * Accede al archivo .JKS donde se almacena la llave privada
     */
    @PostConstruct
    public void init() {
        try {
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/mercadeando.jks");
            keyStore.load(resourceAsStream, SECRECT.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new MercadeandoException("Exception occurred while loading keystore", e);
        }

    }

    /**
     * Genera para un petición de autentificación un token Bearer
     * @param authentication con datos de usuario
     * @return String Bearer Token
     */
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    /**
     * Genera un Bearer token basado en el username
     * @param username nombre de un usuario registrado
     * @return String token
     */
    public String generateTokenWithUserName(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    /**
     * Extrae la llava privada del archivo JKS
     * @return PrivateKey
     */
    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("mercadeando", SECRECT.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            throw new MercadeandoException("Exception occured while retrieving public key from keystore", e);
        }
    }

    /**
     * @param jwt token
     * @return true
     */
    public boolean validateToken(String jwt) {
        parser().setSigningKey(getPublickey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublickey() {
        try {
            return keyStore.getCertificate("mercadeando").getPublicKey();
        } catch (KeyStoreException e) {
            throw new MercadeandoException("Exception occured while " +
                    "retrieving public key from keystore", e);
        }
    }

    /**
     * Extrae el nombre de usuario del Jwt
     * @param token token
     * @return String username
     */
    public String getUsernameFromJwt(String token) {
        Claims claims = parser()
                .setSigningKey(getPublickey())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis() {
        return jwtExpirationInMillis;
    }
}