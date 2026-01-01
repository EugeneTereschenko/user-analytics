/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.dto;

import com.healthcare.prescriptionservice.entity.Route;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationDTO {

    private Long id;

    @NotBlank(message = "Medication name is required")
    @Size(max = 200, message = "Medication name must not exceed 200 characters")
    private String medicationName;

    private String genericName;

    private String drugCode;

    @NotBlank(message = "Dosage is required")
    @Size(max = 100, message = "Dosage must not exceed 100 characters")
    private String dosage;

    private String dosageForm;

    private String strength;

    @NotBlank(message = "Frequency is required")
    @Size(max = 100, message = "Frequency must not exceed 100 characters")
    private String frequency;

    private String duration;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String unit;

    private Route route;

    private String instructions;

    private LocalDate startDate;

    private LocalDate endDate;

    private String warnings;

    private String sideEffects;

    private Boolean isGenericAllowed = true;

    private Boolean isControlledSubstance = false;

    private Integer priority = 1;
}
