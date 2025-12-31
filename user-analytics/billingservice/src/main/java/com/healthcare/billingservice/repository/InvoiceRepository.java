/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.repository;

import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    boolean existsByInvoiceNumber(String invoiceNumber);

    Page<Invoice> findByPatientId(Long patientId, Pageable pageable);

    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE i.status = :status AND i.dueDate < :currentDate")
    List<Invoice> findOverdueInvoices(
            @Param("status") InvoiceStatus status,
            @Param("currentDate") LocalDate currentDate
    );

    @Query("SELECT i FROM Invoice i WHERE i.patientId = :patientId AND i.status = :status")
    List<Invoice> findByPatientIdAndStatus(
            @Param("patientId") Long patientId,
            @Param("status") InvoiceStatus status
    );

    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate BETWEEN :startDate AND :endDate")
    List<Invoice> findInvoicesBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT i FROM Invoice i WHERE i.doctorId = :doctorId " +
            "AND i.invoiceDate BETWEEN :startDate AND :endDate")
    List<Invoice> findDoctorInvoicesBetween(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID' " +
            "AND i.invoiceDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(i.balanceDue) FROM Invoice i WHERE i.status IN ('PENDING', 'SENT', 'PARTIALLY_PAID', 'OVERDUE')")
    BigDecimal getTotalOutstanding();

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.status = 'OVERDUE'")
    Long countOverdueInvoices();

    @Query("SELECT i FROM Invoice i WHERE i.patientId = :patientId " +
            "AND i.balanceDue > 0 ORDER BY i.dueDate ASC")
    List<Invoice> findPatientOutstandingInvoices(@Param("patientId") Long patientId);

    @Query("SELECT i FROM Invoice i WHERE " +
            "LOWER(i.invoiceNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(i.patientName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Invoice> searchInvoices(@Param("searchTerm") String searchTerm, Pageable pageable);
}
