/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.service.impl;

import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PrescriptionService {
    PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO);

    @Transactional(readOnly = true)
    PrescriptionDTO getPrescriptionById(Long id);

    @Transactional(readOnly = true)
    PrescriptionDTO getPrescriptionByNumber(String prescriptionNumber);

    @Transactional(readOnly = true)
    Page<PrescriptionDTO> getAllPrescriptions(Pageable pageable);

    @Transactional(readOnly = true)
    Page<PrescriptionDTO> getPrescriptionsByPatient(Long patientId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PrescriptionDTO> getPrescriptionsByDoctor(Long doctorId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<PrescriptionDTO> getPrescriptionsByStatus(
            PrescriptionStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    List<PrescriptionDTO> getRefillablePrescriptions(Long patientId);

    @Transactional(readOnly = true)
    List<PrescriptionDTO> getExpiredPrescriptions();

    @Transactional(readOnly = true)
    Page<PrescriptionDTO> searchPrescriptions(String searchTerm, Pageable pageable);

    PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO);

    PrescriptionDTO dispensePrescription(Long id, String dispensedBy);

    PrescriptionDTO refillPrescription(Long id);

    PrescriptionDTO cancelPrescription(Long id, String reason);

    void markExpiredPrescriptions();

    void deletePrescription(Long id);
}
