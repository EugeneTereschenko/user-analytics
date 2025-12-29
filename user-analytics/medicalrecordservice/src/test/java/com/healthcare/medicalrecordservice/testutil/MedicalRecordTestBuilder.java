/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.testutil;

import com.healthcare.medicalrecordservice.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordTestBuilder {
    private Long id = 1L;
    private Long patientId = 1L;
    private Long doctorId = 1L;
    private Long appointmentId = 1L;
    private LocalDate recordDate = LocalDate.now();
    private RecordType recordType = RecordType.CONSULTATION;
    private String title = "Test Title";
    private String diagnosis = "Test Diagnosis";
    private String symptoms = "Test Symptoms";
    private String treatment = "Test Treatment";
    private String notes = "Test Notes";
    private List<Prescription> prescriptions = new ArrayList<>();
    private List<LabResult> labResults = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    private List<VitalSign> vitalSigns = new ArrayList<>();
    private String patientName = "John Doe";
    private String doctorName = "Dr. Smith";
    private Boolean isConfidential = false;
    private RecordStatus status = RecordStatus.DRAFT;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime signedAt = null;
    private String signedBy = null;

    public MedicalRecordTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MedicalRecordTestBuilder withPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public MedicalRecordTestBuilder withDoctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public MedicalRecordTestBuilder withAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
        return this;
    }

    public MedicalRecordTestBuilder withRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
        return this;
    }

    public MedicalRecordTestBuilder withRecordType(RecordType recordType) {
        this.recordType = recordType;
        return this;
    }

    public MedicalRecordTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public MedicalRecordTestBuilder withDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public MedicalRecordTestBuilder withSymptoms(String symptoms) {
        this.symptoms = symptoms;
        return this;
    }

    public MedicalRecordTestBuilder withTreatment(String treatment) {
        this.treatment = treatment;
        return this;
    }

    public MedicalRecordTestBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public MedicalRecordTestBuilder withPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
        return this;
    }

    public MedicalRecordTestBuilder withLabResults(List<LabResult> labResults) {
        this.labResults = labResults;
        return this;
    }

    public MedicalRecordTestBuilder withAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public MedicalRecordTestBuilder withVitalSigns(List<VitalSign> vitalSigns) {
        this.vitalSigns = vitalSigns;
        return this;
    }

    public MedicalRecordTestBuilder withPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public MedicalRecordTestBuilder withDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public MedicalRecordTestBuilder withIsConfidential(Boolean isConfidential) {
        this.isConfidential = isConfidential;
        return this;
    }

    public MedicalRecordTestBuilder withStatus(RecordStatus status) {
        this.status = status;
        return this;
    }

    public MedicalRecordTestBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public MedicalRecordTestBuilder withUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public MedicalRecordTestBuilder withSignedAt(LocalDateTime signedAt) {
        this.signedAt = signedAt;
        return this;
    }

    public MedicalRecordTestBuilder withSignedBy(String signedBy) {
        this.signedBy = signedBy;
        return this;
    }

    public MedicalRecord build() {
        return new MedicalRecord(
                id,
                patientId,
                doctorId,
                appointmentId,
                recordDate,
                recordType,
                title,
                diagnosis,
                symptoms,
                treatment,
                notes,
                prescriptions,
                labResults,
                attachments,
                vitalSigns,
                patientName,
                doctorName,
                isConfidential,
                status,
                createdAt,
                updatedAt,
                signedAt,
                signedBy
        );
    }
}
