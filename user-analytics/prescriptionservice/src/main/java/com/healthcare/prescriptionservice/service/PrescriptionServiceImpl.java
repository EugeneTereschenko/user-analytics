/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */


package com.healthcare.prescriptionservice.service;

import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import com.healthcare.prescriptionservice.exception.PrescriptionNotFoundException;
import com.healthcare.prescriptionservice.mapper.PrescriptionMapper;
import com.healthcare.prescriptionservice.repository.PrescriptionRepository;
import com.healthcare.prescriptionservice.service.impl.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;

    @Override
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        log.info("Creating prescription for patient: {}", prescriptionDTO.getPatientId());

        String prescriptionNumber = generatePrescriptionNumber();
        prescriptionDTO.setPrescriptionNumber(prescriptionNumber);

        prescriptionDTO.getUserId();
        Prescription prescription = prescriptionMapper.toEntity(prescriptionDTO);

        // Set bidirectional relationship
        if (prescription.getMedications() != null) {
            prescription.getMedications().forEach(medication ->
                    medication.setPrescription(prescription)
            );
        }

        // Set refills
        if (prescription.getIsRefillable() && prescription.getRefillsAllowed() > 0) {
            prescription.setRefillsRemaining(prescription.getRefillsAllowed());
        }

        Prescription savedPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription created successfully with number: {}", prescriptionNumber);
        return prescriptionMapper.toDTO(savedPrescription);
    }

    @Transactional(readOnly = true)
    @Override
    public PrescriptionDTO getPrescriptionById(Long id) {
        log.info("Fetching prescription with ID: {}", id);

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with ID: " + id));

        return prescriptionMapper.toDTO(prescription);
    }

    @Transactional(readOnly = true)
    @Override
    public PrescriptionDTO getPrescriptionByNumber(String prescriptionNumber) {
        log.info("Fetching prescription with number: {}", prescriptionNumber);

        Prescription prescription = prescriptionRepository.findByPrescriptionNumber(prescriptionNumber)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with number: " + prescriptionNumber));

        return prescriptionMapper.toDTO(prescription);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionDTO> getAllPrescriptions(Pageable pageable) {
        log.info("Fetching all prescriptions with pagination");
        return prescriptionRepository.findAll(pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionDTO> getPrescriptionsByPatient(Long patientId, Pageable pageable) {
        log.info("Fetching prescriptions for patient: {}", patientId);
        return prescriptionRepository.findByPatientId(patientId, pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionDTO> getPrescriptionsByDoctor(Long doctorId, Pageable pageable) {
        log.info("Fetching prescriptions for doctor: {}", doctorId);
        return prescriptionRepository.findByDoctorId(doctorId, pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionDTO> getPrescriptionsByStatus(
            PrescriptionStatus status, Pageable pageable) {
        log.info("Fetching prescriptions with status: {}", status);
        return prescriptionRepository.findByStatus(status, pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrescriptionDTO> getRefillablePrescriptions(Long patientId) {
        log.info("Fetching refillable prescriptions for patient: {}", patientId);
        return prescriptionRepository.findRefillablePrescriptions(patientId)
                .stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrescriptionDTO> getExpiredPrescriptions() {
        log.info("Fetching expired prescriptions");
        return prescriptionRepository.findExpiredPrescriptions(LocalDate.now())
                .stream()
                .map(prescriptionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PrescriptionDTO> searchPrescriptions(String searchTerm, Pageable pageable) {
        log.info("Searching prescriptions with term: {}", searchTerm);
        return prescriptionRepository.searchPrescriptions(searchTerm, pageable)
                .map(prescriptionMapper::toDTO);
    }

    @Override
    public PrescriptionDTO updatePrescription(Long id, PrescriptionDTO prescriptionDTO) {
        log.info("Updating prescription with ID: {}", id);

        Prescription existingPrescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with ID: " + id));

        prescriptionMapper.updateEntityFromDTO(prescriptionDTO, existingPrescription);
        Prescription updatedPrescription = prescriptionRepository.save(existingPrescription);

        log.info("Prescription updated successfully");
        return prescriptionMapper.toDTO(updatedPrescription);
    }

    @Override
    public PrescriptionDTO dispensePrescription(Long id, String dispensedBy) {
        log.info("Dispensing prescription with ID: {}", id);

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with ID: " + id));

        prescription.setStatus(PrescriptionStatus.DISPENSED);
        prescription.setDispensedAt(LocalDateTime.now());
        prescription.setDispensedBy(dispensedBy);

        Prescription dispensedPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription dispensed successfully");
        return prescriptionMapper.toDTO(dispensedPrescription);
    }

    @Override
    public PrescriptionDTO refillPrescription(Long id) {
        log.info("Refilling prescription with ID: {}", id);

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with ID: " + id));

        if (!prescription.getIsRefillable() || prescription.getRefillsRemaining() <= 0) {
            throw new IllegalStateException("Prescription cannot be refilled");
        }

        prescription.setRefillsRemaining(prescription.getRefillsRemaining() - 1);

        if (prescription.getRefillsRemaining() == 0) {
            prescription.setStatus(PrescriptionStatus.COMPLETED);
        }

        Prescription refilledPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription refilled successfully");
        return prescriptionMapper.toDTO(refilledPrescription);
    }

    @Override
    public PrescriptionDTO cancelPrescription(Long id, String reason) {
        log.info("Cancelling prescription with ID: {}", id);

        Prescription prescription = prescriptionRepository.findById(id)
                .orElseThrow(() -> new PrescriptionNotFoundException(
                        "Prescription not found with ID: " + id));

        prescription.setStatus(PrescriptionStatus.CANCELLED);
        prescription.setCancelledAt(LocalDateTime.now());
        prescription.setCancellationReason(reason);

        Prescription cancelledPrescription = prescriptionRepository.save(prescription);

        log.info("Prescription cancelled successfully");
        return prescriptionMapper.toDTO(cancelledPrescription);
    }

    @Override
    public void markExpiredPrescriptions() {
        log.info("Marking expired prescriptions");

        List<Prescription> expiredPrescriptions = prescriptionRepository
                .findExpiredPrescriptions(LocalDate.now());

        expiredPrescriptions.forEach(prescription ->
                prescription.setStatus(PrescriptionStatus.EXPIRED)
        );

        prescriptionRepository.saveAll(expiredPrescriptions);

        log.info("Marked {} prescriptions as expired", expiredPrescriptions.size());
    }

    @Override
    public void deletePrescription(Long id) {
        log.info("Deleting prescription with ID: {}", id);

        if (!prescriptionRepository.existsById(id)) {
            throw new PrescriptionNotFoundException("Prescription not found with ID: " + id);
        }

        prescriptionRepository.deleteById(id);
        log.info("Prescription deleted successfully");
    }

    private String generatePrescriptionNumber() {
        String prefix = "RX";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }
}
