/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.controller;

import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.PatientStatus;
import com.healthcare.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PatientDTO> getPatientByEmail(@PathVariable String email) {
        PatientDTO patient = patientService.getPatientByEmail(email);
        return ResponseEntity.ok(patient);
    }

    @GetMapping
    public ResponseEntity<Page<PatientDTO>> getAllPatients(Pageable pageable) {
        Page<PatientDTO> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientDTO>> searchPatients(
            @RequestParam String query,
            Pageable pageable) {
        Page<PatientDTO> patients = patientService.searchPatients(query, pageable);
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PatientDTO>> getPatientsByStatus(
            @PathVariable PatientStatus status,
            Pageable pageable) {
        Page<PatientDTO> patients = patientService.getPatientsByStatus(status, pageable);
        return ResponseEntity.ok(patients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PatientDTO> deactivatePatient(@PathVariable Long id) {
        PatientDTO deactivatedPatient = patientService.deactivatePatient(id);
        return ResponseEntity.ok(deactivatedPatient);
    }
}