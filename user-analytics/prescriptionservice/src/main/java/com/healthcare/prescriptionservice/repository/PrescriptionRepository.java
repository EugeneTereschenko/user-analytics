/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.repository;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    Optional<Prescription> findByPrescriptionNumber(String prescriptionNumber);

    boolean existsByPrescriptionNumber(String prescriptionNumber);

    Page<Prescription> findByPatientId(Long patientId, Pageable pageable);

    Page<Prescription> findByDoctorId(Long doctorId, Pageable pageable);

    Page<Prescription> findByStatus(PrescriptionStatus status, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId AND p.status = :status")
    List<Prescription> findByPatientIdAndStatus(
            @Param("patientId") Long patientId,
            @Param("status") PrescriptionStatus status
    );

    @Query("SELECT p FROM Prescription p WHERE p.status = 'ACTIVE' AND p.validUntil < :currentDate")
    List<Prescription> findExpiredPrescriptions(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT p FROM Prescription p WHERE p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findPrescriptionsBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId " +
            "AND p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findPatientPrescriptionsBetween(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM Prescription p WHERE p.doctorId = :doctorId " +
            "AND p.prescriptionDate BETWEEN :startDate AND :endDate")
    List<Prescription> findDoctorPrescriptionsBetween(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT p FROM Prescription p WHERE p.patientId = :patientId " +
            "AND p.status IN ('ACTIVE', 'DISPENSED') " +
            "AND p.isRefillable = true AND p.refillsRemaining > 0")
    List<Prescription> findRefillablePrescriptions(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(p) FROM Prescription p WHERE p.status = 'ACTIVE'")
    Long countActivePrescriptions();

    @Query("SELECT p FROM Prescription p WHERE " +
            "LOWER(p.prescriptionNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(p.patientName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Prescription> searchPrescriptions(@Param("searchTerm") String searchTerm, Pageable pageable);
}
