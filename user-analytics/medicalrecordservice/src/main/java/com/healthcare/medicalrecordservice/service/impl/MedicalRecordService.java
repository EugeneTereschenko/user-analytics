/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.service.impl;

import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface MedicalRecordService {
    MedicalRecordDTO createMedicalRecord(MedicalRecordDTO recordDTO);

    @Transactional(readOnly = true)
    MedicalRecordDTO getMedicalRecordById(Long id);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> getAllMedicalRecords(Pageable pageable);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> getMedicalRecordsByDoctor(Long doctorId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> getMedicalRecordsByType(
            RecordType recordType, Pageable pageable);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> getMedicalRecordsByStatus(
            RecordStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    List<MedicalRecordDTO> getMedicalRecordsByAppointment(Long appointmentId);

    @Transactional(readOnly = true)
    List<MedicalRecordDTO> getPatientRecordsBetween(
            Long patientId, LocalDate startDate, LocalDate endDate);

    @Transactional(readOnly = true)
    List<MedicalRecordDTO> getNonConfidentialRecords(Long patientId);

    @Transactional(readOnly = true)
    Long getRecordCountByPatient(Long patientId);

    @Transactional(readOnly = true)
    Page<MedicalRecordDTO> searchMedicalRecords(String searchTerm, Pageable pageable);

    MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO recordDTO);

    MedicalRecordDTO finalizeMedicalRecord(Long id);

    MedicalRecordDTO signMedicalRecord(Long id, String signedBy);

    MedicalRecordDTO archiveMedicalRecord(Long id);

    void deleteMedicalRecord(Long id);
}
