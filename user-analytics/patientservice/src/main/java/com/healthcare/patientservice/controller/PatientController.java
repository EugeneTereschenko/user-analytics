/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.PatientStatus;
import com.healthcare.patientservice.service.PatientServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientServiceImpl patientServiceImpl;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    @RequirePermission(PermissionConstants.PATIENT_CREATE)
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientServiceImpl.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    @RequirePermission(PermissionConstants.PATIENT_READ)
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientServiceImpl.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<PatientDTO> getPatientByEmail(@PathVariable String email) {
        PatientDTO patient = patientServiceImpl.getPatientByEmail(email);
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Page<PatientDTO>> getAllPatients(Pageable pageable) {
        Page<PatientDTO> patients = patientServiceImpl.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Page<PatientDTO>> searchPatients(
            @RequestParam String query,
            Pageable pageable) {
        Page<PatientDTO> patients = patientServiceImpl.searchPatients(query, pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Page<PatientDTO>> getPatientsByStatus(
            @PathVariable PatientStatus status,
            Pageable pageable) {
        Page<PatientDTO> patients = patientServiceImpl.getPatientsByStatus(status, pageable);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientServiceImpl.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientServiceImpl.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<PatientDTO> deactivatePatient(@PathVariable Long id) {
        PatientDTO deactivatedPatient = patientServiceImpl.deactivatePatient(id);
        return ResponseEntity.ok(deactivatedPatient);
    }
}