/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.dto;

import com.healthcare.medicalrecordservice.entity.LabStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
class LabResultDTO {

    private Long id;

    @NotBlank(message = "Test name is required")
    @Size(max = 200, message = "Test name must not exceed 200 characters")
    private String testName;

    private String testCode;

    private String resultValue;

    private String unit;

    private String referenceRange;

    private Boolean isAbnormal = false;

    private String interpretation;

    @NotNull(message = "Test date is required")
    private LocalDate testDate;

    private LocalDate resultDate;

    private String laboratoryName;

    private String performedBy;

    private LabStatus status;

    private LocalDateTime createdAt;
}
