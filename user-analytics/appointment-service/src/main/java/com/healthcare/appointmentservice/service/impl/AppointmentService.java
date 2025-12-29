/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.service.impl;

import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentDTO appointmentDTO);

    @Transactional(readOnly = true)
    AppointmentDTO getAppointmentById(Long id);

    @Transactional(readOnly = true)
    Page<AppointmentDTO> getAllAppointments(Pageable pageable);

    @Transactional(readOnly = true)
    Page<AppointmentDTO> getAppointmentsByPatient(Long patientId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<AppointmentDTO> getAppointmentsByDoctor(Long doctorId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<AppointmentDTO> getAppointmentsByStatus(
            AppointmentStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    List<AppointmentDTO> getAppointmentsBetween(
            LocalDateTime startDate, LocalDateTime endDate);

    @Transactional(readOnly = true)
    List<AppointmentDTO> getTodaysAppointments();

    AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO);

    AppointmentDTO rescheduleAppointment(Long id, LocalDateTime newDateTime);

    AppointmentDTO cancelAppointment(Long id, String reason);

    AppointmentDTO confirmAppointment(Long id);

    AppointmentDTO completeAppointment(Long id, String notes);

    void deleteAppointment(Long id);
}
