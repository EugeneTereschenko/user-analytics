/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.service;

import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.Patient;
import com.healthcare.patientservice.exception.PatientAlreadyExistsException;
import com.healthcare.patientservice.mapper.PatientMapper;
import com.healthcare.patientservice.repository.PatientRepository;
import com.healthcare.patientservice.testutil.PatientTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Service Tests")
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientDTO patientDTO;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO();
        patientDTO.setEmail("test@example.com");
        patient = new PatientTestBuilder()
                .withEmail("test@example.com")
                .build();

    }

    @Test
    @DisplayName("Should create patient successfully")
    void createPatient_Success() {
        when(patientRepository.existsByEmail(patientDTO.getEmail())).thenReturn(false);
        when(patientMapper.toEntity(patientDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toDTO(patient)).thenReturn(patientDTO);

        PatientDTO result = patientService.createPatient(patientDTO);

        assertNotNull(result);
        assertEquals(patientDTO.getEmail(), result.getEmail());
        verify(patientRepository).save(patient);
    }

    @Test
    @DisplayName("Should throw exception if patient already exists")
    void createPatient_AlreadyExists() {
        when(patientRepository.existsByEmail(patientDTO.getEmail())).thenReturn(true);

        assertThrows(PatientAlreadyExistsException.class, () -> patientService.createPatient(patientDTO));
        verify(patientRepository, never()).save(any());
    }
}
