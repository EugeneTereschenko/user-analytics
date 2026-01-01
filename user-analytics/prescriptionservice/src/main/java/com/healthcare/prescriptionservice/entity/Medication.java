/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "medications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    @Column(name = "medication_name", nullable = false, length = 200)
    private String medicationName;

    @Column(name = "generic_name", length = 200)
    private String genericName;

    @Column(name = "drug_code", length = 50)
    private String drugCode;

    @Column(name = "dosage", nullable = false, length = 100)
    private String dosage;

    @Column(name = "dosage_form", length = 50)
    private String dosageForm; // e.g., "Tablet", "Capsule", "Syrup"

    @Column(name = "strength", length = 50)
    private String strength; // e.g., "500mg", "10ml"

    @Column(name = "frequency", nullable = false, length = 100)
    private String frequency; // e.g., "Twice daily", "Every 8 hours"

    @Column(name = "duration", length = 100)
    private String duration; // e.g., "7 days", "2 weeks"

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit", length = 50)
    private String unit; // e.g., "tablets", "ml"

    @Enumerated(EnumType.STRING)
    @Column(name = "route")
    private Route route;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "warnings", columnDefinition = "TEXT")
    private String warnings;

    @Column(name = "side_effects", columnDefinition = "TEXT")
    private String sideEffects;

    @Column(name = "is_generic_allowed")
    private Boolean isGenericAllowed = true;

    @Column(name = "is_controlled_substance")
    private Boolean isControlledSubstance = false;

    @Column(name = "priority")
    private Integer priority = 1;
}
