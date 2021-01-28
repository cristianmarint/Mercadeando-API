package com.api.mercadeando.infrastructure.persistence.jpa;

import com.api.mercadeando.infrastructure.persistence.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoJPARepository extends JpaRepository<Pago, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT * FROM PAGO WHERE pago.paypalPaymentId=:paymentId"
    )
    Optional<Pago> findByPaymentId(String paymentId);
}
