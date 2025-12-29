/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.repository;

import com.healthcare.medicalrecordservice.entity.MedicalRecord;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Page<MedicalRecord> findByPatientId(Long patientId, Pageable pageable);

    Page<MedicalRecord> findByDoctorId(Long doctorId, Pageable pageable);

    Page<MedicalRecord> findByRecordType(RecordType recordType, Pageable pageable);

    Page<MedicalRecord> findByStatus(RecordStatus status, Pageable pageable);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patientId = :patientId AND m.recordType = :recordType")
    List<MedicalRecord> findByPatientIdAndRecordType(
            @Param("patientId") Long patientId,
            @Param("recordType") RecordType recordType
    );

    @Query("SELECT m FROM MedicalRecord m WHERE m.patientId = :patientId " +
            "AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findPatientRecordsBetween(
            @Param("patientId") Long patientId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT m FROM MedicalRecord m WHERE m.doctorId = :doctorId " +
            "AND m.recordDate BETWEEN :startDate AND :endDate")
    List<MedicalRecord> findDoctorRecordsBetween(
            @Param("doctorId") Long doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT m FROM MedicalRecord m WHERE m.appointmentId = :appointmentId")
    List<MedicalRecord> findByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patientId = :patientId " +
            "AND m.isConfidential = false ORDER BY m.recordDate DESC")
    List<MedicalRecord> findNonConfidentialRecordsByPatient(@Param("patientId") Long patientId);

    @Query("SELECT m FROM MedicalRecord m WHERE m.patientId = :patientId " +
            "AND m.status = 'SIGNED' ORDER BY m.recordDate DESC")
    List<MedicalRecord> findSignedRecordsByPatient(@Param("patientId") Long patientId);

    @Query("SELECT COUNT(m) FROM MedicalRecord m WHERE m.patientId = :patientId")
    Long countByPatientId(@Param("patientId") Long patientId);

    @Query("SELECT m FROM MedicalRecord m WHERE " +
            "LOWER(m.diagnosis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.symptoms) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(m.title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<MedicalRecord> searchRecords(@Param("searchTerm") String searchTerm, Pageable pageable);
}
