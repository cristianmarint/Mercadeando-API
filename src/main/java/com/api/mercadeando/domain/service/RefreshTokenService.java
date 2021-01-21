package com.api.mercadeando.domain.service;

import com.api.mercadeando.infrastructure.persistence.entity.RefreshToken;
import com.api.mercadeando.domain.exception.MercadeandoException;
import com.api.mercadeando.infrastructure.persistence.jpa.RefreshTokenJPARepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {
    private final RefreshTokenJPARepository refreshTokenJPARepository;

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedAt(Timestamp.from(Instant.now()));

        return refreshTokenJPARepository.save(refreshToken);
    }

    void validateRefreshToken(String token){
        refreshTokenJPARepository
                .findByToken(token)
                .orElseThrow(()-> new MercadeandoException("Invalid refresh Token"));
    }

    public void deleteRefreshToken(String token){
        refreshTokenJPARepository.deleteByToken(token);
    }
}
