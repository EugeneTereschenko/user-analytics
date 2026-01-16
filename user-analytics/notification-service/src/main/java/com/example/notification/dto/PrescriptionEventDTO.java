/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionEventDTO implements Serializable {
    private Long prescriptionId;
    private Long patientId;
    private String patientName;
    private String patientEmail;
    private String patientPhone;
    private String medicationName;
    private String dosage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String eventType; // CREATED, READY, REMINDER
}
