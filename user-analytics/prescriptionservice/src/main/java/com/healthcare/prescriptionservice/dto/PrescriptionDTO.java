/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.dto;

import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

    private Long id;

    private String prescriptionNumber;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long medicalRecordId;

    private Long appointmentId;

    @NotNull(message = "Prescription date is required")
    private LocalDate prescriptionDate;

    @Future(message = "Valid until date must be in the future")
    private LocalDate validUntil;

    private PrescriptionStatus status;

    @NotEmpty(message = "Prescription must have at least one medication")
    private List<MedicationDTO> medications;

    private String patientName;

    @Past(message = "Patient date of birth must be in the past")
    private LocalDate patientDateOfBirth;

    private String doctorName;

    private String doctorLicense;

    private String pharmacyName;

    private String pharmacyAddress;

    @Size(max = 500, message = "Diagnosis must not exceed 500 characters")
    private String diagnosis;

    private String notes;

    private Boolean isRefillable = true;

    @Min(value = 0, message = "Refills allowed cannot be negative")
    private Integer refillsAllowed = 0;

    private Integer refillsRemaining = 0;

    private LocalDateTime dispensedAt;

    private String dispensedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;
}