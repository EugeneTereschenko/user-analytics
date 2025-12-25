/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.service.impl;

import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.PatientStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PatientService {
    PatientDTO createPatient(PatientDTO patientDTO);
    PatientDTO getPatientById(Long id);

    @Transactional(readOnly = true)
    PatientDTO getPatientByEmail(String email);

    @Transactional(readOnly = true)
    Page<PatientDTO> getAllPatients(Pageable pageable);

    @Transactional(readOnly = true)
    Page<PatientDTO> searchPatients(String searchTerm, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PatientDTO> getPatientsByStatus(PatientStatus status, Pageable pageable);

    PatientDTO updatePatient(Long id, PatientDTO patientDTO);

    void deletePatient(Long id);

    PatientDTO deactivatePatient(Long id);
}
