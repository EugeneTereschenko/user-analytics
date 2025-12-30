package com.healthcare.doctorservice.mapper;

import com.healthcare.doctorservice.dto.DoctorDTO;
import com.healthcare.doctorservice.entity.Doctor;
import com.healthcare.doctorservice.entity.DoctorStatus;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setEmail(doctor.getEmail());
        dto.setPhoneNumber(doctor.getPhoneNumber());
        dto.setDateOfBirth(doctor.getDateOfBirth());
        dto.setGender(doctor.getGender());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setQualifications(doctor.getQualifications());
        dto.setLanguages(doctor.getLanguages());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setBiography(doctor.getBiography());
        dto.setStatus(doctor.getStatus());
        dto.setJoinedDate(doctor.getJoinedDate());
        dto.setProfileImageUrl(doctor.getProfileImageUrl());
        dto.setDepartment(doctor.getDepartment());
        dto.setRoomNumber(doctor.getRoomNumber());
        dto.setEmergencyContactName(doctor.getEmergencyContactName());
        dto.setEmergencyContactPhone(doctor.getEmergencyContactPhone());
        dto.setCreatedAt(doctor.getCreatedAt());
        dto.setUpdatedAt(doctor.getUpdatedAt());
        return dto;
    }

    public Doctor toEntity(DoctorDTO dto) {
        if (dto == null) return null;

        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(dto.getEmail());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setDateOfBirth(dto.getDateOfBirth());
        doctor.setGender(dto.getGender());
        doctor.setLicenseNumber(dto.getLicenseNumber());
        doctor.setSpecialization(dto.getSpecialization());
        doctor.setQualifications(dto.getQualifications());
        doctor.setLanguages(dto.getLanguages());
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setConsultationFee(dto.getConsultationFee());
        doctor.setBiography(dto.getBiography());
        doctor.setStatus(dto.getStatus() != null ? dto.getStatus() : DoctorStatus.ACTIVE);
        doctor.setJoinedDate(dto.getJoinedDate());
        doctor.setProfileImageUrl(dto.getProfileImageUrl());
        doctor.setDepartment(dto.getDepartment());
        doctor.setRoomNumber(dto.getRoomNumber());
        doctor.setEmergencyContactName(dto.getEmergencyContactName());
        doctor.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        return doctor;
    }

    public void updateEntityFromDTO(DoctorDTO dto, Doctor doctor) {
        if (dto.getFirstName() != null) doctor.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) doctor.setLastName(dto.getLastName());
        if (dto.getEmail() != null) doctor.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) doctor.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) doctor.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) doctor.setGender(dto.getGender());
        if (dto.getLicenseNumber() != null) doctor.setLicenseNumber(dto.getLicenseNumber());
        if (dto.getSpecialization() != null) doctor.setSpecialization(dto.getSpecialization());
        if (dto.getQualifications() != null) doctor.setQualifications(dto.getQualifications());
        if (dto.getLanguages() != null) doctor.setLanguages(dto.getLanguages());
        if (dto.getYearsOfExperience() != null) doctor.setYearsOfExperience(dto.getYearsOfExperience());
        if (dto.getConsultationFee() != null) doctor.setConsultationFee(dto.getConsultationFee());
        if (dto.getBiography() != null) doctor.setBiography(dto.getBiography());
        if (dto.getStatus() != null) doctor.setStatus(dto.getStatus());
        if (dto.getDepartment() != null) doctor.setDepartment(dto.getDepartment());
        if (dto.getRoomNumber() != null) doctor.setRoomNumber(dto.getRoomNumber());
    }
}
