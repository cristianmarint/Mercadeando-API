package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenJPARepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUserId(Long id);
}
