/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.mapper;

import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.MedicalRecord;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import org.springframework.stereotype.Component;

@Component
public class MedicalRecordMapper {

    public MedicalRecordDTO toDTO(MedicalRecord record) {
        if (record == null) {
            return null;
        }

        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(record.getId());
        dto.setPatientId(record.getPatientId());
        dto.setDoctorId(record.getDoctorId());
        dto.setAppointmentId(record.getAppointmentId());
        dto.setRecordDate(record.getRecordDate());
        dto.setRecordType(record.getRecordType());
        dto.setTitle(record.getTitle());
        dto.setDiagnosis(record.getDiagnosis());
        dto.setSymptoms(record.getSymptoms());
        dto.setTreatment(record.getTreatment());
        dto.setNotes(record.getNotes());
        dto.setPatientName(record.getPatientName());
        dto.setDoctorName(record.getDoctorName());
        dto.setIsConfidential(record.getIsConfidential());
        dto.setStatus(record.getStatus());
        dto.setCreatedAt(record.getCreatedAt());
        dto.setUpdatedAt(record.getUpdatedAt());
        dto.setSignedAt(record.getSignedAt());
        dto.setSignedBy(record.getSignedBy());
        dto.setUserId(record.getUserId());

        // Map nested collections if needed
        // dto.setPrescriptions(...);
        // dto.setLabResults(...);
        // dto.setAttachments(...);
        // dto.setVitalSigns(...);

        return dto;
    }

    public MedicalRecord toEntity(MedicalRecordDTO dto) {
        if (dto == null) {
            return null;
        }

        MedicalRecord record = new MedicalRecord();
        record.setId(dto.getId());
        record.setPatientId(dto.getPatientId());
        record.setDoctorId(dto.getDoctorId());
        record.setAppointmentId(dto.getAppointmentId());
        record.setRecordDate(dto.getRecordDate());
        record.setRecordType(dto.getRecordType());
        record.setTitle(dto.getTitle());
        record.setDiagnosis(dto.getDiagnosis());
        record.setSymptoms(dto.getSymptoms());
        record.setTreatment(dto.getTreatment());
        record.setNotes(dto.getNotes());
        record.setPatientName(dto.getPatientName());
        record.setDoctorName(dto.getDoctorName());
        record.setIsConfidential(dto.getIsConfidential());
        record.setStatus(dto.getStatus() != null ? dto.getStatus() : RecordStatus.DRAFT);
        record.setUserId(dto.getUserId());

        return record;
    }

    public void updateEntityFromDTO(MedicalRecordDTO dto, MedicalRecord record) {
        if (dto.getRecordDate() != null) record.setRecordDate(dto.getRecordDate());
        if (dto.getRecordType() != null) record.setRecordType(dto.getRecordType());
        if (dto.getTitle() != null) record.setTitle(dto.getTitle());
        if (dto.getDiagnosis() != null) record.setDiagnosis(dto.getDiagnosis());
        if (dto.getSymptoms() != null) record.setSymptoms(dto.getSymptoms());
        if (dto.getTreatment() != null) record.setTreatment(dto.getTreatment());
        if (dto.getNotes() != null) record.setNotes(dto.getNotes());
        if (dto.getPatientName() != null) record.setPatientName(dto.getPatientName());
        if (dto.getDoctorName() != null) record.setDoctorName(dto.getDoctorName());
        if (dto.getIsConfidential() != null) record.setIsConfidential(dto.getIsConfidential());
        if (dto.getStatus() != null) record.setStatus(dto.getStatus());
        if (dto.getUserId() != null) record.setUserId(dto.getUserId());
    }
}
