/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.mapper;

import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.entity.PatientStatus;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDTO toDTO(Patient patient) {
        if (patient == null) {
            return null;
        }

        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setLastName(patient.getLastName());
        dto.setEmail(patient.getEmail());
        dto.setPhoneNumber(patient.getPhoneNumber());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setGender(patient.getGender());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setMedicalNotes(patient.getMedicalNotes());
        dto.setStatus(patient.getStatus());
        dto.setCreatedAt(patient.getCreatedAt());
        dto.setUpdatedAt(patient.getUpdatedAt());
        dto.setAllergies(patient.getAllergies());

        // Map address and emergency contacts as needed

        return dto;
    }

    public Patient toEntity(PatientDTO dto) {
        if (dto == null) {
            return null;
        }

        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setEmail(dto.getEmail());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setMedicalNotes(dto.getMedicalNotes());
        patient.setStatus(dto.getStatus() != null ? dto.getStatus() : PatientStatus.ACTIVE);
        patient.setAllergies(dto.getAllergies());

        // Map address and emergency contacts as needed

        return patient;
    }

    public void updateEntityFromDTO(PatientDTO dto, Patient patient) {
        if (dto.getFirstName() != null) patient.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) patient.setLastName(dto.getLastName());
        if (dto.getEmail() != null) patient.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) patient.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) patient.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) patient.setGender(dto.getGender());
        if (dto.getBloodGroup() != null) patient.setBloodGroup(dto.getBloodGroup());
        if (dto.getMedicalNotes() != null) patient.setMedicalNotes(dto.getMedicalNotes());
        if (dto.getStatus() != null) patient.setStatus(dto.getStatus());
        if (dto.getAllergies() != null) patient.setAllergies(dto.getAllergies());
    }
}
