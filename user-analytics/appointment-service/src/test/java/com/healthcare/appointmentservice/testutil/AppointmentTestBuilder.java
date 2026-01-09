package com.healthcare.appointmentservice.testutil;

import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import com.healthcare.appointmentservice.entity.AppointmentType;

import java.time.LocalDateTime;

public class AppointmentTestBuilder {
    private Long id;
    private Long patientId = 1L;
    private Long doctorId = 1L;
    private Long userId = 1L;
    private LocalDateTime appointmentDateTime = LocalDateTime.of(2025, 6, 2, 10, 0);
    private Integer durationMinutes = 30;
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
    private AppointmentType appointmentType = AppointmentType.CONSULTATION;
    private String reason = "Routine checkup";
    private String notes = "No additional notes";
    private String patientName = "John Doe";
    private String patientEmail = "john.doe@example.com";
    private String patientPhone = "+1234567890";
    private String doctorName = "Dr. Smith";
    private String doctorSpecialization = "General Medicine";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime cancelledAt;
    private String cancellationReason;

    public AppointmentTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public AppointmentTestBuilder withPatientId(Long patientId) {
        this.patientId = patientId;
        return this;
    }

    public AppointmentTestBuilder withDoctorId(Long doctorId) {
        this.doctorId = doctorId;
        return this;
    }

    public AppointmentTestBuilder withUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public AppointmentTestBuilder withAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
        return this;
    }

    public AppointmentTestBuilder withDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        return this;
    }

    public AppointmentTestBuilder withStatus(AppointmentStatus status) {
        this.status = status;
        return this;
    }

    public AppointmentTestBuilder withAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
        return this;
    }

    public AppointmentTestBuilder withReason(String reason) {
        this.reason = reason;
        return this;
    }

    public AppointmentTestBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public AppointmentTestBuilder withPatientName(String patientName) {
        this.patientName = patientName;
        return this;
    }

    public AppointmentTestBuilder withPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
        return this;
    }

    public AppointmentTestBuilder withPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
        return this;
    }

    public AppointmentTestBuilder withDoctorName(String doctorName) {
        this.doctorName = doctorName;
        return this;
    }

    public AppointmentTestBuilder withDoctorSpecialization(String doctorSpecialization) {
        this.doctorSpecialization = doctorSpecialization;
        return this;
    }

    public AppointmentTestBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AppointmentTestBuilder withUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public AppointmentTestBuilder withCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
        return this;
    }

    public AppointmentTestBuilder withCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
        return this;
    }

    public Appointment build() {
        return new Appointment(
                id,
                patientId,
                doctorId,
                userId,
                appointmentDateTime,
                durationMinutes,
                status,
                appointmentType,
                reason,
                notes,
                patientName,
                patientEmail,
                patientPhone,
                doctorName,
                doctorSpecialization,
                createdAt,
                updatedAt,
                cancelledAt,
                cancellationReason
        );
    }

    public static AppointmentDTO defaultAppointmentDTO() {
        return new AppointmentDTO(
                null, // id
                1L, // patientId
                1L, // doctorId
                1L, // userId
                LocalDateTime.of(2026, 6, 2, 10, 0),
                30,
                AppointmentStatus.SCHEDULED,
                AppointmentType.CONSULTATION,
                "Routine checkup",
                "No additional notes",
                "John Doe",
                "john.doe@example.com",
                "+1234567890",
                "Dr. Smith",
                "General Medicine",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                null
        );
    }

    public static Appointment defaultAppointment() {
        return new AppointmentTestBuilder().build();
    }

    public static Appointment defaultSavedAppointment() {
        return new AppointmentTestBuilder()
                .withId(100L)
                .build();
    }
}

