/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.repository;

import com.healthcare.billingservice.entity.Payment;
import com.healthcare.billingservice.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentReference(String paymentReference);

    List<Payment> findByInvoiceId(Long invoiceId);

    @Query("SELECT p FROM Payment p WHERE p.invoice.patientId = :patientId " +
            "ORDER BY p.paymentDate DESC")
    List<Payment> findByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalPaymentsBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT p FROM Payment p WHERE p.paymentMethod = :method " +
            "AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findPaymentsByMethodBetween(
            @Param("method") PaymentMethod method,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
