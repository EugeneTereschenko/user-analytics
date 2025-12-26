/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.mapper;

import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentDTO toDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setPatientId(appointment.getPatientId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setAppointmentType(appointment.getAppointmentType());
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());
        dto.setPatientName(appointment.getPatientName());
        dto.setPatientEmail(appointment.getPatientEmail());
        dto.setPatientPhone(appointment.getPatientPhone());
        dto.setDoctorName(appointment.getDoctorName());
        dto.setDoctorSpecialization(appointment.getDoctorSpecialization());
        dto.setCreatedAt(appointment.getCreatedAt());
        dto.setUpdatedAt(appointment.getUpdatedAt());
        dto.setCancelledAt(appointment.getCancelledAt());
        dto.setCancellationReason(appointment.getCancellationReason());

        return dto;
    }

    public Appointment toEntity(AppointmentDTO dto) {
        if (dto == null) {
            return null;
        }

        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        appointment.setDurationMinutes(dto.getDurationMinutes());
        appointment.setStatus(dto.getStatus() != null ? dto.getStatus() : AppointmentStatus.SCHEDULED);
        appointment.setAppointmentType(dto.getAppointmentType());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        appointment.setPatientName(dto.getPatientName());
        appointment.setPatientEmail(dto.getPatientEmail());
        appointment.setPatientPhone(dto.getPatientPhone());
        appointment.setDoctorName(dto.getDoctorName());
        appointment.setDoctorSpecialization(dto.getDoctorSpecialization());
        appointment.setCancellationReason(dto.getCancellationReason());

        return appointment;
    }

    public void updateEntityFromDTO(AppointmentDTO dto, Appointment appointment) {
        if (dto.getAppointmentDateTime() != null)
            appointment.setAppointmentDateTime(dto.getAppointmentDateTime());
        if (dto.getDurationMinutes() != null)
            appointment.setDurationMinutes(dto.getDurationMinutes());
        if (dto.getStatus() != null)
            appointment.setStatus(dto.getStatus());
        if (dto.getAppointmentType() != null)
            appointment.setAppointmentType(dto.getAppointmentType());
        if (dto.getReason() != null)
            appointment.setReason(dto.getReason());
        if (dto.getNotes() != null)
            appointment.setNotes(dto.getNotes());
        if (dto.getPatientName() != null)
            appointment.setPatientName(dto.getPatientName());
        if (dto.getPatientEmail() != null)
            appointment.setPatientEmail(dto.getPatientEmail());
        if (dto.getPatientPhone() != null)
            appointment.setPatientPhone(dto.getPatientPhone());
        if (dto.getDoctorName() != null)
            appointment.setDoctorName(dto.getDoctorName());
        if (dto.getDoctorSpecialization() != null)
            appointment.setDoctorSpecialization(dto.getDoctorSpecialization());
    }
}
