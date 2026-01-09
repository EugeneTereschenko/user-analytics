package com.healthcare.doctorservice.mapper;

import com.healthcare.doctorservice.dto.StaffDTO;
import com.healthcare.doctorservice.entity.Staff;
import com.healthcare.doctorservice.entity.StaffStatus;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public StaffDTO toDTO(Staff staff) {
        if (staff == null) return null;

        StaffDTO dto = new StaffDTO();
        dto.setId(staff.getId());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        dto.setPhoneNumber(staff.getPhoneNumber());
        dto.setDateOfBirth(staff.getDateOfBirth());
        dto.setGender(staff.getGender());
        dto.setEmployeeId(staff.getEmployeeId());
        dto.setRole(staff.getRole());
        dto.setDepartment(staff.getDepartment());
        dto.setShift(staff.getShift());
        dto.setSalary(staff.getSalary());
        dto.setJoinedDate(staff.getJoinedDate());
        dto.setStatus(staff.getStatus());
        dto.setSupervisorId(staff.getSupervisorId());
        dto.setEmergencyContactName(staff.getEmergencyContactName());
        dto.setEmergencyContactPhone(staff.getEmergencyContactPhone());
        dto.setProfileImageUrl(staff.getProfileImageUrl());
        dto.setUserId(staff.getUserId());
        dto.setCreatedAt(staff.getCreatedAt());
        dto.setUpdatedAt(staff.getUpdatedAt());
        return dto;
    }

    public Staff toEntity(StaffDTO dto) {
        if (dto == null) return null;

        Staff staff = new Staff();
        staff.setId(dto.getId());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setPhoneNumber(dto.getPhoneNumber());
        staff.setDateOfBirth(dto.getDateOfBirth());
        staff.setGender(dto.getGender());
        staff.setEmployeeId(dto.getEmployeeId());
        staff.setRole(dto.getRole());
        staff.setDepartment(dto.getDepartment());
        staff.setShift(dto.getShift());
        staff.setSalary(dto.getSalary());
        staff.setJoinedDate(dto.getJoinedDate());
        staff.setStatus(dto.getStatus() != null ? dto.getStatus() : StaffStatus.ACTIVE);
        staff.setSupervisorId(dto.getSupervisorId());
        staff.setEmergencyContactName(dto.getEmergencyContactName());
        staff.setEmergencyContactPhone(dto.getEmergencyContactPhone());
        staff.setProfileImageUrl(dto.getProfileImageUrl());
        staff.setUserId(dto.getUserId());
        return staff;
    }

    public void updateEntityFromDTO(StaffDTO dto, Staff staff) {
        if (dto.getFirstName() != null) staff.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) staff.setLastName(dto.getLastName());
        if (dto.getEmail() != null) staff.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) staff.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getDateOfBirth() != null) staff.setDateOfBirth(dto.getDateOfBirth());
        if (dto.getGender() != null) staff.setGender(dto.getGender());
        if (dto.getEmployeeId() != null) staff.setEmployeeId(dto.getEmployeeId());
        if (dto.getRole() != null) staff.setRole(dto.getRole());
        if (dto.getDepartment() != null) staff.setDepartment(dto.getDepartment());
        if (dto.getShift() != null) staff.setShift(dto.getShift());
        if (dto.getSalary() != null) staff.setSalary(dto.getSalary());
        if (dto.getStatus() != null) staff.setStatus(dto.getStatus());
        if (dto.getSupervisorId() != null) staff.setSupervisorId(dto.getSupervisorId());
        if (dto.getUserId() != null) staff.setUserId(dto.getUserId());
    }
}
