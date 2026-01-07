package com.healthcare.doctorservice.controller;

import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.StaffRole;
import com.healthcare.doctorservice.entity.StaffStatus;
import com.healthcare.doctorservice.service.impl.StaffService;
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
@RequestMapping("/api/v1/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<StaffDTO> createStaff(@Valid @RequestBody StaffDTO staffDTO) {
        StaffDTO created = staffService.createStaff(staffDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<StaffDTO> getStaffById(@PathVariable Long id) {
        StaffDTO staff = staffService.getStaffById(id);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StaffDTO> getStaffByEmail(@PathVariable String email) {
        StaffDTO staff = staffService.getStaffByEmail(email);
        return ResponseEntity.ok(staff);
    }

    @GetMapping
    public ResponseEntity<Page<StaffDTO>> getAllStaff(Pageable pageable) {
        Page<StaffDTO> staff = staffService.getAllStaff(pageable);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<Page<StaffDTO>> getStaffByRole(
            @PathVariable StaffRole role,
            Pageable pageable) {
        Page<StaffDTO> staff = staffService.getStaffByRole(role, pageable);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<StaffDTO>> getStaffByStatus(
            @PathVariable StaffStatus status,
            Pageable pageable) {
        Page<StaffDTO> staff = staffService.getStaffByStatus(status, pageable);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<Page<StaffDTO>> getStaffByDepartment(
            @PathVariable String department,
            Pageable pageable) {
        Page<StaffDTO> staff = staffService.getStaffByDepartment(department, pageable);
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StaffDTO>> getActiveStaff() {
        List<StaffDTO> staff = staffService.getActiveStaff();
        return ResponseEntity.ok(staff);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<StaffDTO>> searchStaff(
            @RequestParam String query,
            Pageable pageable) {
        Page<StaffDTO> staff = staffService.searchStaff(query, pageable);
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StaffDTO> updateStaff(
            @PathVariable Long id,
            @Valid @RequestBody StaffDTO staffDTO) {
        StaffDTO updated = staffService.updateStaff(id, staffDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<StaffDTO> updateStaffStatus(
            @PathVariable Long id,
            @RequestParam StaffStatus status) {
        StaffDTO updated = staffService.updateStaffStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }
}
