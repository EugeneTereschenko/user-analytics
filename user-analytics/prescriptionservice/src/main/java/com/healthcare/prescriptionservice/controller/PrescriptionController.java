package com.healthcare.prescriptionservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import com.healthcare.prescriptionservice.service.impl.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    //@RequirePermission(PermissionConstants.BILLING_CREATE)
    public ResponseEntity<PrescriptionDTO> createPrescription(
            @Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO created = prescriptionService.createPrescription(prescriptionDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.PRESCRIPTION_READ)
    public ResponseEntity<PrescriptionDTO> getPrescriptionById(@PathVariable Long id) {
        PrescriptionDTO prescription = prescriptionService.getPrescriptionById(id);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping("/number/{prescriptionNumber}")
    public ResponseEntity<PrescriptionDTO> getPrescriptionByNumber(
            @PathVariable String prescriptionNumber) {
        PrescriptionDTO prescription = prescriptionService
                .getPrescriptionByNumber(prescriptionNumber);
        return ResponseEntity.ok(prescription);
    }

    @GetMapping
    public ResponseEntity<Page<PrescriptionDTO>> getAllPrescriptions(Pageable pageable) {
        Page<PrescriptionDTO> prescriptions = prescriptionService.getAllPrescriptions(pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<PrescriptionDTO>> getPrescriptionsByPatient(
            @PathVariable Long patientId,
            Pageable pageable) {
        Page<PrescriptionDTO> prescriptions = prescriptionService
                .getPrescriptionsByPatient(patientId, pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<PrescriptionDTO>> getPrescriptionsByDoctor(
            @PathVariable Long doctorId,
            Pageable pageable) {
        Page<PrescriptionDTO> prescriptions = prescriptionService
                .getPrescriptionsByDoctor(doctorId, pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PrescriptionDTO>> getPrescriptionsByStatus(
            @PathVariable PrescriptionStatus status,
            Pageable pageable) {
        Page<PrescriptionDTO> prescriptions = prescriptionService
                .getPrescriptionsByStatus(status, pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/patient/{patientId}/refillable")
    public ResponseEntity<List<PrescriptionDTO>> getRefillablePrescriptions(
            @PathVariable Long patientId) {
        List<PrescriptionDTO> prescriptions = prescriptionService
                .getRefillablePrescriptions(patientId);
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<PrescriptionDTO>> getExpiredPrescriptions() {
        List<PrescriptionDTO> prescriptions = prescriptionService.getExpiredPrescriptions();
        return ResponseEntity.ok(prescriptions);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PrescriptionDTO>> searchPrescriptions(
            @RequestParam String query,
            Pageable pageable) {
        Page<PrescriptionDTO> prescriptions = prescriptionService
                .searchPrescriptions(query, pageable);
        return ResponseEntity.ok(prescriptions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrescriptionDTO> updatePrescription(
            @PathVariable Long id,
            @Valid @RequestBody PrescriptionDTO prescriptionDTO) {
        PrescriptionDTO updated = prescriptionService.updatePrescription(id, prescriptionDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/dispense")
    public ResponseEntity<PrescriptionDTO> dispensePrescription(
            @PathVariable Long id,
            @RequestParam String dispensedBy) {
        PrescriptionDTO dispensed = prescriptionService.dispensePrescription(id, dispensedBy);
        return ResponseEntity.ok(dispensed);
    }

    @PatchMapping("/{id}/refill")
    public ResponseEntity<PrescriptionDTO> refillPrescription(@PathVariable Long id) {
        PrescriptionDTO refilled = prescriptionService.refillPrescription(id);
        return ResponseEntity.ok(refilled);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PrescriptionDTO> cancelPrescription(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        PrescriptionDTO cancelled = prescriptionService.cancelPrescription(id, reason);
        return ResponseEntity.ok(cancelled);
    }

    @PostMapping("/mark-expired")
    public ResponseEntity<Void> markExpiredPrescriptions() {
        prescriptionService.markExpiredPrescriptions();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> deletePrescription(@PathVariable Long id) {
        prescriptionService.deletePrescription(id);
        return ResponseEntity.noContent().build();
    }
}
