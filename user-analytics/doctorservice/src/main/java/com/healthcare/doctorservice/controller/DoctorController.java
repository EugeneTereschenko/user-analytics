package com.healthcare.doctorservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.DoctorStatus;
import com.healthcare.doctorservice.service.impl.DoctorService;
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
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR')")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO created = doctorService.createDoctor(doctorDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        DoctorDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@PathVariable String email) {
        DoctorDTO doctor = doctorService.getDoctorByEmail(email);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<DoctorDTO> getDoctorByLicenseNumber(@PathVariable String licenseNumber) {
        DoctorDTO doctor = doctorService.getDoctorByLicenseNumber(licenseNumber);
        return ResponseEntity.ok(doctor);
    }

    @GetMapping
    public ResponseEntity<Page<DoctorDTO>> getAllDoctors(Pageable pageable) {
        Page<DoctorDTO> doctors = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<Page<DoctorDTO>> getDoctorsBySpecialization(
            @PathVariable String specialization,
            Pageable pageable) {
        Page<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(specialization, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<DoctorDTO>> getDoctorsByStatus(
            @PathVariable DoctorStatus status,
            Pageable pageable) {
        Page<DoctorDTO> doctors = doctorService.getDoctorsByStatus(status, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<Page<DoctorDTO>> getDoctorsByDepartment(
            @PathVariable String department,
            Pageable pageable) {
        Page<DoctorDTO> doctors = doctorService.getDoctorsByDepartment(department, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/active")
    public ResponseEntity<List<DoctorDTO>> getActiveDoctors() {
        List<DoctorDTO> doctors = doctorService.getActiveDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<DoctorDTO>> searchDoctors(
            @RequestParam String query,
            Pageable pageable) {
        Page<DoctorDTO> doctors = doctorService.searchDoctors(query, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specializations")
    public ResponseEntity<List<String>> getAllSpecializations() {
        List<String> specializations = doctorService.getAllSpecializations();
        return ResponseEntity.ok(specializations);
    }

    @GetMapping("/departments")
    public ResponseEntity<List<String>> getAllDepartments() {
        List<String> departments = doctorService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorDTO updated = doctorService.updateDoctor(id, doctorDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DoctorDTO> updateDoctorStatus(
            @PathVariable Long id,
            @RequestParam DoctorStatus status) {
        DoctorDTO updated = doctorService.updateDoctorStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }
}
