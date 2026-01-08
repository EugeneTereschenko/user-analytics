/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.service;

import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.entity.AppointmentStatus;
import com.healthcare.appointmentservice.exception.AppointmentConflictException;
import com.healthcare.appointmentservice.exception.AppointmentNotFoundException;
import com.healthcare.appointmentservice.exception.InvalidAppointmentException;
import com.healthcare.appointmentservice.mapper.AppointmentMapper;
import com.healthcare.appointmentservice.repository.AppointmentRepository;
import com.healthcare.appointmentservice.service.impl.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating appointment for patient: {} with doctor: {}",
                appointmentDTO.getPatientId(), appointmentDTO.getDoctorId());

        validateAppointmentTime(appointmentDTO.getAppointmentDateTime());

        LocalDateTime endTime = appointmentDTO.getAppointmentDateTime()
                .plusMinutes(appointmentDTO.getDurationMinutes());

        if (appointmentRepository.existsDoctorConflict(
                appointmentDTO.getDoctorId(),
                appointmentDTO.getAppointmentDateTime(),
                endTime)) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment at this time");
        }

        appointmentDTO.checkUserId();

        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment created successfully with ID: {}", savedAppointment.getId());
        return appointmentMapper.toDTO(savedAppointment);
    }

    @Transactional(readOnly = true)
    @Override
    public AppointmentDTO getAppointmentById(Long id) {
        log.info("Fetching appointment with ID: {}", id);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        return appointmentMapper.toDTO(appointment);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentDTO> getAllAppointments(Pageable pageable) {
        log.info("Fetching all appointments with pagination");
        return appointmentRepository.findAll(pageable)
                .map(appointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentDTO> getAppointmentsByPatient(Long patientId, Pageable pageable) {
        log.info("Fetching appointments for patient: {}", patientId);
        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(appointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentDTO> getAppointmentsByDoctor(Long doctorId, Pageable pageable) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        return appointmentRepository.findByDoctorId(doctorId, pageable)
                .map(appointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AppointmentDTO> getAppointmentsByStatus(
            AppointmentStatus status, Pageable pageable) {
        log.info("Fetching appointments with status: {}", status);
        return appointmentRepository.findByStatus(status, pageable)
                .map(appointmentMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentDTO> getAppointmentsBetween(
            LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching appointments between {} and {}", startDate, endDate);
        return appointmentRepository.findAppointmentsBetween(startDate, endDate)
                .stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<AppointmentDTO> getTodaysAppointments() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);

        log.info("Fetching today's appointments");
        return appointmentRepository.findTodaysAppointments(today, tomorrow)
                .stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment with ID: {}", id);

        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        if (appointmentDTO.getAppointmentDateTime() != null) {
            validateAppointmentTime(appointmentDTO.getAppointmentDateTime());
        }

        appointmentMapper.updateEntityFromDTO(appointmentDTO, existingAppointment);
        Appointment updatedAppointment = appointmentRepository.save(existingAppointment);

        log.info("Appointment updated successfully with ID: {}", id);
        return appointmentMapper.toDTO(updatedAppointment);
    }

    @Override
    public AppointmentDTO rescheduleAppointment(Long id, LocalDateTime newDateTime) {
        log.info("Rescheduling appointment {} to {}", id, newDateTime);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        validateAppointmentTime(newDateTime);

        LocalDateTime endTime = newDateTime.plusMinutes(appointment.getDurationMinutes());

        if (appointmentRepository.existsDoctorConflict(
                appointment.getDoctorId(), newDateTime, endTime)) {
            throw new AppointmentConflictException(
                    "Doctor already has an appointment at this time");
        }

        appointment.setAppointmentDateTime(newDateTime);
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment updatedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment rescheduled successfully");
        return appointmentMapper.toDTO(updatedAppointment);
    }

    @Override
    public AppointmentDTO cancelAppointment(Long id, String reason) {
        log.info("Cancelling appointment with ID: {}", id);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancelledAt(LocalDateTime.now());
        appointment.setCancellationReason(reason);

        Appointment cancelledAppointment = appointmentRepository.save(appointment);

        log.info("Appointment cancelled successfully");
        return appointmentMapper.toDTO(cancelledAppointment);
    }

    @Override
    public AppointmentDTO confirmAppointment(Long id) {
        log.info("Confirming appointment with ID: {}", id);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        Appointment confirmedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment confirmed successfully");
        return appointmentMapper.toDTO(confirmedAppointment);
    }

    @Override
    public AppointmentDTO completeAppointment(Long id, String notes) {
        log.info("Completing appointment with ID: {}", id);

        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(
                        "Appointment not found with ID: " + id));

        appointment.setStatus(AppointmentStatus.COMPLETED);
        if (notes != null) {
            appointment.setNotes(notes);
        }

        Appointment completedAppointment = appointmentRepository.save(appointment);

        log.info("Appointment completed successfully");
        return appointmentMapper.toDTO(completedAppointment);
    }

    @Override
    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with ID: {}", id);

        if (!appointmentRepository.existsById(id)) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + id);
        }

        appointmentRepository.deleteById(id);
        log.info("Appointment deleted successfully");
    }

    private void validateAppointmentTime(LocalDateTime appointmentTime) {
        if (appointmentTime.isBefore(LocalDateTime.now())) {
            throw new InvalidAppointmentException(
                    "Appointment time cannot be in the past");
        }

        int hour = appointmentTime.getHour();
        if (hour < 8 || hour >= 18) {
            throw new InvalidAppointmentException(
                    "Appointments can only be scheduled between 8 AM and 6 PM");
        }

        int dayOfWeek = appointmentTime.getDayOfWeek().getValue();
        if (dayOfWeek == 6 || dayOfWeek == 7) {
            throw new InvalidAppointmentException(
                    "Appointments cannot be scheduled on weekends");
        }
    }
}
