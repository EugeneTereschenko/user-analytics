/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.dto;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import com.healthcare.appointmentservice.entity.AppointmentType;
import com.healthcare.appointmentservice.exception.UnauthorizedException;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long userId;

    @NotNull(message = "Appointment date and time is required")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDateTime;

    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration cannot exceed 8 hours")
    private Integer durationMinutes = 30;

    private AppointmentStatus status;

    @NotNull(message = "Appointment type is required")
    private AppointmentType appointmentType;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private String patientName;

    @Email(message = "Patient email must be valid")
    private String patientEmail;

    private String patientPhone;

    private String doctorName;

    private String doctorSpecialization;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    public void checkUserId() {
        if (this.userId == null) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                    .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
            this.userId = currentUserId;
        }
    }
}