/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.service;

import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.entity.PatientStatus;
import com.healthcare.patientservice.exception.PatientAlreadyExistsException;
import com.healthcare.patientservice.exception.PatientNotFoundException;
import com.healthcare.patientservice.mapper.PatientMapper;
import com.healthcare.patientservice.repository.PatientRepository;
import com.healthcare.patientservice.service.impl.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        log.info("Creating new patient with email: {}", patientDTO.getEmail());

        if (patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new PatientAlreadyExistsException("Patient with email " + patientDTO.getEmail() + " already exists");
        }

        Patient patient = patientMapper.toEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);

        log.info("Patient created successfully with ID: {}", savedPatient.getId());
        return patientMapper.toDTO(savedPatient);
    }

    @Transactional(readOnly = true)
    @Override
    public PatientDTO getPatientById(Long id) {
        log.info("Fetching patient with ID: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        return patientMapper.toDTO(patient);
    }

    @Transactional(readOnly = true)
    @Override
    public PatientDTO getPatientByEmail(String email) {
        log.info("Fetching patient with email: {}", email);

        Patient patient = patientRepository.findByEmail(email)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with email: " + email));

        return patientMapper.toDTO(patient);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PatientDTO> getAllPatients(Pageable pageable) {
        log.info("Fetching all patients with pagination");

        return patientRepository.findAll(pageable)
                .map(patientMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PatientDTO> searchPatients(String searchTerm, Pageable pageable) {
        log.info("Searching patients with term: {}", searchTerm);

        return patientRepository.searchPatients(searchTerm, pageable)
                .map(patientMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<PatientDTO> getPatientsByStatus(PatientStatus status, Pageable pageable) {
        log.info("Fetching patients with status: {}", status);

        return patientRepository.findByStatus(status, pageable)
                .map(patientMapper::toDTO);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        log.info("Updating patient with ID: {}", id);

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (!existingPatient.getEmail().equals(patientDTO.getEmail())
                && patientRepository.existsByEmail(patientDTO.getEmail())) {
            throw new PatientAlreadyExistsException("Patient with email " + patientDTO.getEmail() + " already exists");
        }

        patientMapper.updateEntityFromDTO(patientDTO, existingPatient);
        Patient updatedPatient = patientRepository.save(existingPatient);

        log.info("Patient updated successfully with ID: {}", updatedPatient.getId());
        return patientMapper.toDTO(updatedPatient);
    }

    @Override
    public void deletePatient(Long id) {
        log.info("Deleting patient with ID: {}", id);

        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with ID: " + id);
        }

        patientRepository.deleteById(id);
        log.info("Patient deleted successfully with ID: {}", id);
    }

    @Override
    public PatientDTO deactivatePatient(Long id) {
        log.info("Deactivating patient with ID: {}", id);

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with ID: " + id));

        patient.setStatus(PatientStatus.INACTIVE);
        Patient updatedPatient = patientRepository.save(patient);

        log.info("Patient deactivated successfully with ID: {}", id);
        return patientMapper.toDTO(updatedPatient);
    }
}
