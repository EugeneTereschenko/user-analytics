/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.controller;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.constants.PermissionConstants;
import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import com.healthcare.appointmentservice.service.AppointmentServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentServiceImpl appointmentServiceImpl;

    @PostMapping
    //@RequirePermission(PermissionConstants.APPOINTMENT_CREATE)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<AppointmentDTO> createAppointment(
            @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO created = appointmentServiceImpl.createAppointment(appointmentDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @RequirePermission(PermissionConstants.APPOINTMENT_READ)
    public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointment = appointmentServiceImpl.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentDTO>> getAllAppointments(Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentServiceImpl.getAllAppointments(pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentServiceImpl
                .getAppointmentsByPatient(patientId, pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByDoctor(
            @PathVariable Long doctorId,
            Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentServiceImpl
                .getAppointmentsByDoctor(doctorId, pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<AppointmentDTO>> getAppointmentsByStatus(
            @PathVariable AppointmentStatus status,
            Pageable pageable) {
        Page<AppointmentDTO> appointments = appointmentServiceImpl
                .getAppointmentsByStatus(status, pageable);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/between")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate) {
        List<AppointmentDTO> appointments = appointmentServiceImpl
                .getAppointmentsBetween(startDate, endDate);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/today")
    public ResponseEntity<List<AppointmentDTO>> getTodaysAppointments() {
        List<AppointmentDTO> appointments = appointmentServiceImpl.getTodaysAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDTO> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody AppointmentDTO appointmentDTO) {
        AppointmentDTO updated = appointmentServiceImpl.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentDTO> rescheduleAppointment(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime newDateTime) {
        AppointmentDTO rescheduled = appointmentServiceImpl.rescheduleAppointment(id, newDateTime);
        return ResponseEntity.ok(rescheduled);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDTO> cancelAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        AppointmentDTO cancelled = appointmentServiceImpl.cancelAppointment(id, reason);
        return ResponseEntity.ok(cancelled);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<AppointmentDTO> confirmAppointment(@PathVariable Long id) {
        AppointmentDTO confirmed = appointmentServiceImpl.confirmAppointment(id);
        return ResponseEntity.ok(confirmed);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<AppointmentDTO> completeAppointment(
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {
        AppointmentDTO completed = appointmentServiceImpl.completeAppointment(id, notes);
        return ResponseEntity.ok(completed);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DOCTOR', 'ROLE_STAFF')")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentServiceImpl.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
