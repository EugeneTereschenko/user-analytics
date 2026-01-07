/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
import com.healthcare.medicalrecordservice.service.impl.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    //@RequirePermission(PermissionConstants.BILLING_CREATE)
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(
            @Valid @RequestBody MedicalRecordDTO recordDTO) {
        MedicalRecordDTO created = medicalRecordService.createMedicalRecord(recordDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.MEDICAL_RECORD_READ)
    public ResponseEntity<MedicalRecordDTO> getMedicalRecordById(@PathVariable Long id) {
        MedicalRecordDTO record = medicalRecordService.getMedicalRecordById(id);
        return ResponseEntity.ok(record);
    }

    @GetMapping
    public ResponseEntity<Page<MedicalRecordDTO>> getAllMedicalRecords(Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService.getAllMedicalRecords(pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<MedicalRecordDTO>> getMedicalRecordsByPatient(
            @PathVariable Long patientId,
            Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService
                .getMedicalRecordsByPatient(patientId, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<MedicalRecordDTO>> getMedicalRecordsByDoctor(
            @PathVariable Long doctorId,
            Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService
                .getMedicalRecordsByDoctor(doctorId, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/type/{recordType}")
    public ResponseEntity<Page<MedicalRecordDTO>> getMedicalRecordsByType(
            @PathVariable RecordType recordType,
            Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService
                .getMedicalRecordsByType(recordType, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<MedicalRecordDTO>> getMedicalRecordsByStatus(
            @PathVariable RecordStatus status,
            Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService
                .getMedicalRecordsByStatus(status, pageable);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecordsByAppointment(
            @PathVariable Long appointmentId) {
        List<MedicalRecordDTO> records = medicalRecordService
                .getMedicalRecordsByAppointment(appointmentId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}/between")
    public ResponseEntity<List<MedicalRecordDTO>> getPatientRecordsBetween(
            @PathVariable Long patientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<MedicalRecordDTO> records = medicalRecordService
                .getPatientRecordsBetween(patientId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}/non-confidential")
    public ResponseEntity<List<MedicalRecordDTO>> getNonConfidentialRecords(
            @PathVariable Long patientId) {
        List<MedicalRecordDTO> records = medicalRecordService
                .getNonConfidentialRecords(patientId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/patient/{patientId}/count")
    public ResponseEntity<Long> getRecordCount(@PathVariable Long patientId) {
        Long count = medicalRecordService.getRecordCountByPatient(patientId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<MedicalRecordDTO>> searchMedicalRecords(
            @RequestParam String query,
            Pageable pageable) {
        Page<MedicalRecordDTO> records = medicalRecordService
                .searchMedicalRecords(query, pageable);
        return ResponseEntity.ok(records);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordDTO recordDTO) {
        MedicalRecordDTO updated = medicalRecordService.updateMedicalRecord(id, recordDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/finalize")
    public ResponseEntity<MedicalRecordDTO> finalizeMedicalRecord(@PathVariable Long id) {
        MedicalRecordDTO finalized = medicalRecordService.finalizeMedicalRecord(id);
        return ResponseEntity.ok(finalized);
    }

    @PatchMapping("/{id}/sign")
    public ResponseEntity<MedicalRecordDTO> signMedicalRecord(
            @PathVariable Long id,
            @RequestParam String signedBy) {
        MedicalRecordDTO signed = medicalRecordService.signMedicalRecord(id, signedBy);
        return ResponseEntity.ok(signed);
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<MedicalRecordDTO> archiveMedicalRecord(@PathVariable Long id) {
        MedicalRecordDTO archived = medicalRecordService.archiveMedicalRecord(id);
        return ResponseEntity.ok(archived);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }
}
