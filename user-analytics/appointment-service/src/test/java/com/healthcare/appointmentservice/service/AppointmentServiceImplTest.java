/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.service;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.appointmentservice.dto.AppointmentDTO;
import com.healthcare.appointmentservice.entity.Appointment;
import com.healthcare.appointmentservice.exception.AppointmentConflictException;
import com.healthcare.appointmentservice.mapper.AppointmentMapper;
import com.healthcare.appointmentservice.repository.AppointmentRepository;
import com.healthcare.appointmentservice.service.AppointmentServiceImpl;
import com.healthcare.appointmentservice.testutil.AppointmentTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Appointment Service Tests")
class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private AppointmentDTO appointmentDTO;
    private Appointment appointment;
    private Appointment savedAppointment;

    @BeforeEach
    void setUp() {
        appointmentDTO = AppointmentTestBuilder.defaultAppointmentDTO();
        appointment = AppointmentTestBuilder.defaultAppointment();
        savedAppointment = AppointmentTestBuilder.defaultSavedAppointment();
    }

    @Test
    @DisplayName("Should create appointment successfully")
    void createAppointment_success() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(Optional.of(1L));

            when(appointmentRepository.existsDoctorConflict(anyLong(), any(), any())).thenReturn(false);
            when(appointmentMapper.toEntity(appointmentDTO)).thenReturn(appointment);
            when(appointmentRepository.save(appointment)).thenReturn(savedAppointment);
            when(appointmentMapper.toDTO(savedAppointment)).thenReturn(appointmentDTO);

            AppointmentDTO result = appointmentService.createAppointment(appointmentDTO);

            assertNotNull(result);
            verify(appointmentRepository).save(appointment);
            verify(appointmentMapper).toDTO(savedAppointment);
        }
    }



    @Test
    @DisplayName("Should throw conflict exception if doctor has appointment")
    void createAppointment_doctorConflict() {
        when(appointmentRepository.existsDoctorConflict(
                anyLong(), any(), any())).thenReturn(true);

        assertThrows(AppointmentConflictException.class,
                () -> appointmentService.createAppointment(appointmentDTO));
        verify(appointmentRepository, never()).save(any());
    }
}
