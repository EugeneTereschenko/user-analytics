package com.healthcare.prescriptionservice.testutil;

import com.healthcare.prescriptionservice.entity.Medication;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.Route;

import java.time.LocalDate;

public class MedicationTestBuilder {
    private Long id = 1L;
    private Prescription prescription = null;
    private String medicationName = "Paracetamol";
    private String genericName = "Acetaminophen";
    private String drugCode = "PCT500";
    private String dosage = "500mg";
    private String dosageForm = "Tablet";
    private String strength = "500mg";
    private String frequency = "Twice daily";
    private String duration = "7 days";
    private Integer quantity = 14;
    private String unit = "tablets";
    private Route route = Route.ORAL;
    private String instructions = "Take after meals";
    private LocalDate startDate = LocalDate.now();
    private LocalDate endDate = LocalDate.now().plusDays(7);
    private String warnings = "Do not exceed recommended dose";
    private String sideEffects = "Nausea, rash";
    private Boolean isGenericAllowed = true;
    private Boolean isControlledSubstance = false;
    private Integer priority = 1;

    public MedicationTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MedicationTestBuilder withPrescription(Prescription prescription) {
        this.prescription = prescription;
        return this;
    }

    public MedicationTestBuilder withMedicationName(String medicationName) {
        this.medicationName = medicationName;
        return this;
    }

    // Add similar withX methods for other fields as needed...

    public Medication build() {
        return new Medication(
                id,
                prescription,
                medicationName,
                genericName,
                drugCode,
                dosage,
                dosageForm,
                strength,
                frequency,
                duration,
                quantity,
                unit,
                route,
                instructions,
                startDate,
                endDate,
                warnings,
                sideEffects,
                isGenericAllowed,
                isControlledSubstance,
                priority
        );
    }

    public static MedicationTestBuilder defaultBuilder() {
        return new MedicationTestBuilder();
    }
}

