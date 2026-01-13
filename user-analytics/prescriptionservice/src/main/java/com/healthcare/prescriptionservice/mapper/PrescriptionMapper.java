/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.mapper;

import com.healthcare.prescriptionservice.dto.MedicationDTO;
import com.healthcare.prescriptionservice.dto.PrescriptionDTO;
import com.healthcare.prescriptionservice.entity.Medication;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PrescriptionMapper {

    public PrescriptionDTO toDTO(Prescription prescription) {
        if (prescription == null) return null;

        PrescriptionDTO dto = new PrescriptionDTO();
        dto.setId(prescription.getId());
        dto.setPrescriptionNumber(prescription.getPrescriptionNumber());
        dto.setPatientId(prescription.getPatientId());
        dto.setDoctorId(prescription.getDoctorId());
        dto.setMedicalRecordId(prescription.getMedicalRecordId());
        dto.setAppointmentId(prescription.getAppointmentId());
        dto.setPrescriptionDate(prescription.getPrescriptionDate());
        dto.setValidUntil(prescription.getValidUntil());
        dto.setStatus(prescription.getStatus());
        dto.setPatientName(prescription.getPatientName());
        dto.setPatientDateOfBirth(prescription.getPatientDateOfBirth());
        dto.setDoctorName(prescription.getDoctorName());
        dto.setDoctorLicense(prescription.getDoctorLicense());
        dto.setPharmacyName(prescription.getPharmacyName());
        dto.setPharmacyAddress(prescription.getPharmacyAddress());
        dto.setDiagnosis(prescription.getDiagnosis());
        dto.setNotes(prescription.getNotes());
        dto.setIsRefillable(prescription.getIsRefillable());
        dto.setRefillsAllowed(prescription.getRefillsAllowed());
        dto.setRefillsRemaining(prescription.getRefillsRemaining());
        dto.setDispensedAt(prescription.getDispensedAt());
        dto.setDispensedBy(prescription.getDispensedBy());
        dto.setCreatedAt(prescription.getCreatedAt());
        dto.setUpdatedAt(prescription.getUpdatedAt());
        dto.setCancelledAt(prescription.getCancelledAt());
        dto.setCancellationReason(prescription.getCancellationReason());
        dto.setUserId(prescription.getUserId());

        if (prescription.getMedications() != null) {
            dto.setMedications(prescription.getMedications().stream()
                    .map(this::medicationToDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public Prescription toEntity(PrescriptionDTO dto) {
        if (dto == null) return null;

        Prescription prescription = new Prescription();
        prescription.setId(dto.getId());
        prescription.setPrescriptionNumber(dto.getPrescriptionNumber());
        prescription.setPatientId(dto.getPatientId());
        prescription.setDoctorId(dto.getDoctorId());
        prescription.setMedicalRecordId(dto.getMedicalRecordId());
        prescription.setAppointmentId(dto.getAppointmentId());
        prescription.setPrescriptionDate(dto.getPrescriptionDate());
        prescription.setValidUntil(dto.getValidUntil());
        prescription.setStatus(dto.getStatus() != null ? dto.getStatus() : PrescriptionStatus.ACTIVE);
        prescription.setPatientName(dto.getPatientName());
        prescription.setPatientDateOfBirth(dto.getPatientDateOfBirth());
        prescription.setDoctorName(dto.getDoctorName());
        prescription.setDoctorLicense(dto.getDoctorLicense());
        prescription.setPharmacyName(dto.getPharmacyName());
        prescription.setPharmacyAddress(dto.getPharmacyAddress());
        prescription.setDiagnosis(dto.getDiagnosis());
        prescription.setNotes(dto.getNotes());
        prescription.setIsRefillable(dto.getIsRefillable());
        prescription.setRefillsAllowed(dto.getRefillsAllowed());
        prescription.setRefillsRemaining(dto.getRefillsRemaining());
        prescription.setUserId(dto.getUserId());

        if (dto.getMedications() != null) {
            prescription.setMedications(dto.getMedications().stream()
                    .map(this::medicationToEntity)
                    .collect(Collectors.toList()));
        }

        return prescription;
    }

    public void updateEntityFromDTO(PrescriptionDTO dto, Prescription prescription) {
        if (dto.getValidUntil() != null) prescription.setValidUntil(dto.getValidUntil());
        if (dto.getStatus() != null) prescription.setStatus(dto.getStatus());
        if (dto.getNotes() != null) prescription.setNotes(dto.getNotes());
        if (dto.getPharmacyName() != null) prescription.setPharmacyName(dto.getPharmacyName());
        if (dto.getPharmacyAddress() != null) prescription.setPharmacyAddress(dto.getPharmacyAddress());
        if (dto.getUserId() != null) prescription.setUserId(dto.getUserId());
    }

    private MedicationDTO medicationToDTO(Medication medication) {
        if (medication == null) return null;

        MedicationDTO dto = new MedicationDTO();
        dto.setId(medication.getId());
        dto.setMedicationName(medication.getMedicationName());
        dto.setGenericName(medication.getGenericName());
        dto.setDrugCode(medication.getDrugCode());
        dto.setDosage(medication.getDosage());
        dto.setDosageForm(medication.getDosageForm());
        dto.setStrength(medication.getStrength());
        dto.setFrequency(medication.getFrequency());
        dto.setDuration(medication.getDuration());
        dto.setQuantity(medication.getQuantity());
        dto.setUnit(medication.getUnit());
        dto.setRoute(medication.getRoute());
        dto.setInstructions(medication.getInstructions());
        dto.setStartDate(medication.getStartDate());
        dto.setEndDate(medication.getEndDate());
        dto.setWarnings(medication.getWarnings());
        dto.setSideEffects(medication.getSideEffects());
        dto.setIsGenericAllowed(medication.getIsGenericAllowed());
        dto.setIsControlledSubstance(medication.getIsControlledSubstance());
        dto.setPriority(medication.getPriority());

        return dto;
    }

    private Medication medicationToEntity(MedicationDTO dto) {
        if (dto == null) return null;

        Medication medication = new Medication();
        medication.setId(dto.getId());
        medication.setMedicationName(dto.getMedicationName());
        medication.setGenericName(dto.getGenericName());
        medication.setDrugCode(dto.getDrugCode());
        medication.setDosage(dto.getDosage());
        medication.setDosageForm(dto.getDosageForm());
        medication.setStrength(dto.getStrength());
        medication.setFrequency(dto.getFrequency());
        medication.setDuration(dto.getDuration());
        medication.setQuantity(dto.getQuantity());
        medication.setUnit(dto.getUnit());
        medication.setRoute(dto.getRoute());
        medication.setInstructions(dto.getInstructions());
        medication.setStartDate(dto.getStartDate());
        medication.setEndDate(dto.getEndDate());
        medication.setWarnings(dto.getWarnings());
        medication.setSideEffects(dto.getSideEffects());
        medication.setIsGenericAllowed(dto.getIsGenericAllowed());
        medication.setIsControlledSubstance(dto.getIsControlledSubstance());
        medication.setPriority(dto.getPriority());

        return medication;
    }
}
