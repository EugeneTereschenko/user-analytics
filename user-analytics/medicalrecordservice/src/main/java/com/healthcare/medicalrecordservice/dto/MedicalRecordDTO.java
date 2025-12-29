/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.dto;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.entity.RecordType;
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
public class MedicalRecordDTO {

    private Long id;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @NotNull(message = "Doctor ID is required")
    private Long doctorId;

    private Long appointmentId;

    @NotNull(message = "Record date is required")
    @PastOrPresent(message = "Record date cannot be in the future")
    private LocalDate recordDate;

    @NotNull(message = "Record type is required")
    private RecordType recordType;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    private String diagnosis;

    private String symptoms;

    private String treatment;

    private String notes;

    private List<PrescriptionDTO> prescriptions;

    private List<LabResultDTO> labResults;

    private List<AttachmentDTO> attachments;

    private List<VitalSignDTO> vitalSigns;

    private String patientName;

    private String doctorName;

    private Boolean isConfidential = false;

    private RecordStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime signedAt;

    private String signedBy;
}
