/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */


package com.healthcare.medicalrecordservice.service;

import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
import com.healthcare.medicalrecordservice.service.impl.MedicalRecordService;


import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.MedicalRecord;
import com.healthcare.medicalrecordservice.exception.MedicalRecordNotFoundException;
import com.healthcare.medicalrecordservice.mapper.MedicalRecordMapper;
import com.healthcare.medicalrecordservice.repository.MedicalRecordRepository;
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
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    @Override
    public MedicalRecordDTO createMedicalRecord(MedicalRecordDTO recordDTO) {
        log.info("Creating medical record for patient: {}", recordDTO.getPatientId());

        MedicalRecord record = medicalRecordMapper.toEntity(recordDTO);
        MedicalRecord savedRecord = medicalRecordRepository.save(record);

        log.info("Medical record created successfully with ID: {}", savedRecord.getId());
        return medicalRecordMapper.toDTO(savedRecord);
    }

    @Transactional(readOnly = true)
    @Override
    public MedicalRecordDTO getMedicalRecordById(Long id) {
        log.info("Fetching medical record with ID: {}", id);

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(
                        "Medical record not found with ID: " + id));

        return medicalRecordMapper.toDTO(record);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> getAllMedicalRecords(Pageable pageable) {
        log.info("Fetching all medical records with pagination");
        return medicalRecordRepository.findAll(pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> getMedicalRecordsByPatient(Long patientId, Pageable pageable) {
        log.info("Fetching medical records for patient: {}", patientId);
        return medicalRecordRepository.findByPatientId(patientId, pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> getMedicalRecordsByDoctor(Long doctorId, Pageable pageable) {
        log.info("Fetching medical records for doctor: {}", doctorId);
        return medicalRecordRepository.findByDoctorId(doctorId, pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> getMedicalRecordsByType(
            RecordType recordType, Pageable pageable) {
        log.info("Fetching medical records of type: {}", recordType);
        return medicalRecordRepository.findByRecordType(recordType, pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> getMedicalRecordsByStatus(
            RecordStatus status, Pageable pageable) {
        log.info("Fetching medical records with status: {}", status);
        return medicalRecordRepository.findByStatus(status, pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MedicalRecordDTO> getMedicalRecordsByAppointment(Long appointmentId) {
        log.info("Fetching medical records for appointment: {}", appointmentId);
        return medicalRecordRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<MedicalRecordDTO> getPatientRecordsBetween(
            Long patientId, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching patient {} records between {} and {}",
                patientId, startDate, endDate);
        return medicalRecordRepository.findPatientRecordsBetween(patientId, startDate, endDate)
                .stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<MedicalRecordDTO> getNonConfidentialRecords(Long patientId) {
        log.info("Fetching non-confidential records for patient: {}", patientId);
        return medicalRecordRepository.findNonConfidentialRecordsByPatient(patientId)
                .stream()
                .map(medicalRecordMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Long getRecordCountByPatient(Long patientId) {
        log.info("Counting records for patient: {}", patientId);
        return medicalRecordRepository.countByPatientId(patientId);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MedicalRecordDTO> searchMedicalRecords(String searchTerm, Pageable pageable) {
        log.info("Searching medical records with term: {}", searchTerm);
        return medicalRecordRepository.searchRecords(searchTerm, pageable)
                .map(medicalRecordMapper::toDTO);
    }

    @Override
    public MedicalRecordDTO updateMedicalRecord(Long id, MedicalRecordDTO recordDTO) {
        log.info("Updating medical record with ID: {}", id);

        MedicalRecord existingRecord = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(
                        "Medical record not found with ID: " + id));

        medicalRecordMapper.updateEntityFromDTO(recordDTO, existingRecord);
        MedicalRecord updatedRecord = medicalRecordRepository.save(existingRecord);

        log.info("Medical record updated successfully with ID: {}", id);
        return medicalRecordMapper.toDTO(updatedRecord);
    }

    @Override
    public MedicalRecordDTO finalizeMedicalRecord(Long id) {
        log.info("Finalizing medical record with ID: {}", id);

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(
                        "Medical record not found with ID: " + id));

        record.setStatus(RecordStatus.FINALIZED);
        MedicalRecord finalizedRecord = medicalRecordRepository.save(record);

        log.info("Medical record finalized successfully");
        return medicalRecordMapper.toDTO(finalizedRecord);
    }

    @Override
    public MedicalRecordDTO signMedicalRecord(Long id, String signedBy) {
        log.info("Signing medical record with ID: {} by {}", id, signedBy);

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(
                        "Medical record not found with ID: " + id));

        record.setStatus(RecordStatus.SIGNED);
        record.setSignedAt(LocalDateTime.now());
        record.setSignedBy(signedBy);

        MedicalRecord signedRecord = medicalRecordRepository.save(record);

        log.info("Medical record signed successfully");
        return medicalRecordMapper.toDTO(signedRecord);
    }

    @Override
    public MedicalRecordDTO archiveMedicalRecord(Long id) {
        log.info("Archiving medical record with ID: {}", id);

        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException(
                        "Medical record not found with ID: " + id));

        record.setStatus(RecordStatus.ARCHIVED);
        MedicalRecord archivedRecord = medicalRecordRepository.save(record);

        log.info("Medical record archived successfully");
        return medicalRecordMapper.toDTO(archivedRecord);
    }

    @Override
    public void deleteMedicalRecord(Long id) {
        log.info("Deleting medical record with ID: {}", id);

        if (!medicalRecordRepository.existsById(id)) {
            throw new MedicalRecordNotFoundException(
                    "Medical record not found with ID: " + id);
        }

        medicalRecordRepository.deleteById(id);
        log.info("Medical record deleted successfully");
    }
}
