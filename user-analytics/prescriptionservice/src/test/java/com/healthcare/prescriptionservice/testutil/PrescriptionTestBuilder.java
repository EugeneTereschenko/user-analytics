/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.testutil;

import com.healthcare.prescriptionservice.entity.Medication;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionTestBuilder {
    private Long id = 1L;
    private String prescriptionNumber = "RX-123456";
    private Long patientId = 100L;
    private Long doctorId = 200L;
    private Long medicalRecordId = 300L;
    private Long appointmentId = 400L;
    private LocalDate prescriptionDate = LocalDate.now();
    private LocalDate validUntil = LocalDate.now().plusDays(30);
    private PrescriptionStatus status = PrescriptionStatus.ACTIVE;
    private List<Medication> medications = new ArrayList<>();
    private String patientName = "John Doe";
    private LocalDate patientDateOfBirth = LocalDate.of(1980, 1, 1);
    private String doctorName = "Dr. Smith";
    private String doctorLicense = "DOC12345";
    private String pharmacyName = "Best Pharmacy";
    private String pharmacyAddress = "123 Main St";
    private String diagnosis = "Common Cold";
    private String notes = "Take as prescribed";
    private Boolean isRefillable = true;
    private Integer refillsAllowed = 2;
    private Integer refillsRemaining = 2;
    private LocalDateTime dispensedAt = null;
    private String dispensedBy = null;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime cancelledAt = null;
    private String cancellationReason = null;

    public PrescriptionTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PrescriptionTestBuilder withPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
        return this;
    }

    public PrescriptionTestBuilder withPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public PrescriptionTestBuilder withDoctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public PrescriptionTestBuilder withMedications(List<Medication> medications) {
        this.medications = medications;
        return this;
    }

    public PrescriptionTestBuilder withStatus(PrescriptionStatus status) {
        this.status = status;
        return this;
    }

    public PrescriptionTestBuilder withValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    // Add similar withX methods for other fields as needed...

    public Prescription build() {
        Prescription prescription = new Prescription(
                id,
                prescriptionNumber,
                patientId,
                doctorId,
                medicalRecordId,
                appointmentId,
                prescriptionDate,
                validUntil,
                status,
                medications,
                patientName,
                patientDateOfBirth,
                doctorName,
                doctorLicense,
                pharmacyName,
                pharmacyAddress,
                diagnosis,
                notes,
                isRefillable,
                refillsAllowed,
                refillsRemaining,
                dispensedAt,
                dispensedBy,
                createdAt,
                updatedAt,
                cancelledAt,
                cancellationReason
        );
        if (medications != null) {
            for (Medication med : medications) {
                med.setPrescription(prescription);
            }
        }
        return prescription;
    }


    public static PrescriptionTestBuilder defaultBuilder() {
        return new PrescriptionTestBuilder();
    }
}

